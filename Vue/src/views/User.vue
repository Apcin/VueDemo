<template>
  <div class="home" style="padding: 10px">

    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>

    <div style="margin: 10px 0">
        <el-input v-model="search" placeholder="请输入关键字" style="width: 30%" clearable></el-input>
        <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
    </div>

   <el-table :data="tableData" border stripe style="width: 100%">
    <el-table-column prop="id" label="ID" sortable/>
    <el-table-column prop="username" label="用户名" />
    <el-table-column prop="nickName" label="昵称" />
    <el-table-column prop="age" label="年龄" />
    <el-table-column prop="gender" label="性别" />
    <el-table-column prop="address" label="地址" />
    <el-table-column label="操作">
      <template #default="scope">
        <el-button size="big" @click="handleEdit(scope.row)"
          >编辑</el-button
        >
        <el-popconfirm title="确定删除吗?" @confirm="handleDelete(scope.row.id)">
          <template #reference>
          <el-button size="big" type="danger">删除
          </el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
   </el-table>
   <div style="margin: 10px 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
     <el-dialog v-model="dialogVisible" title="提示" width="30%">
       <el-form :model="form" label-width="120px">
         <el-form-item label="用户名">
           <el-input v-model="form.username" style="width: 80%"/>
         </el-form-item>
         <el-form-item label="昵称">
           <el-input v-model="form.nickName" style="width: 80%"/>
         </el-form-item>
         <el-form-item label="年龄">
           <el-input v-model="form.age" style="width: 80%"/>
         </el-form-item>
         <el-form-item label="性别">
           <el-radio v-model="form.gender" label="1" size="large">男</el-radio>
           <el-radio v-model="form.gender" label="2" size="large">女</el-radio>
           <el-radio v-model="form.gender" label="3" size="large">未知</el-radio>
         </el-form-item>
         <el-form-item label="地址">
           <el-input type="textarea" v-model="form.address" style="width: 80%"/>
         </el-form-item>
       </el-form>
       <template #footer>
         <span class="dialog-footer">
           <el-button @click="dialogVisible = false">取消</el-button>
           <el-button type="primary" @click="save">确定</el-button>
         </span>
       </template>
     </el-dialog>
   </div>
  </div>

</template>

<script>
import request from "@/utils/request";

export default {
  name: 'Home',
  components: {

  },
  data(){
    return{
    form:{},
    dialogVisible:false,
    search:'',
    currentPage: 1,
    total:0,
    pageSize:10,
    tableData :[

  ]
     }
  },
  created() {
    this.load()
  },
  methods: {
     load(){
        request.get("/user",{
          params:{
             pageNum:this.currentPage,
             pageSize:this.pageSize,
             search:this.search
          }
       }).then(res =>{
          console.log(res)
          const {records} = res.data;
          this.tableData=records
          this.total=res.data.total
        })
     },
     add() {
       this.dialogVisible = true
       this.form = {}
     },
     save() {
       if(this.form.id)//更新
       {
         request.put("/user",this.form).then((res) =>{
         console.log(res)
         if(res.code==='0')
         {
            this.$message({
            type:"success",
            message:"更新成功"
          })
         }
         else{
             this.$message({
             type:"error",
             message:res.msg
          })
         }
         })
       }
       else{ //新增
         request.post("/user",this.form).then((res) =>{
         console.log(res)
          if(res.code==='0')
         {
            this.$message({
            type:"success",
            message:"新增成功"
          })
         }
         else{
             this.$message({
             type:"error",
             message:res.msg
          })
         }
         })
       }
         this.load() //表格刷新
         this.dialogVisible = false
     },
     handleEdit(row) {
         this.form=JSON.parse(JSON.stringify(row))
         this.dialogVisible = true
     },
     handleSizeChange(pageSize){  //改变当前每页的个数触发
         this.pageSize = pageSize
         this.load()
     },
     handleCurrentChange(pageNum){//改变当前页码触发
         this.currentPage = pageNum
         this.load()
     },handleDelete(id){
         console.log(id)
         request.delete("/user/"+id).then(res=>{
         if (res.code === '0') {
             this.$message({
               type: "success",
               message: "删除成功"
             })
           } else {
             this.$message({
               type: "error",
               message: res.msg
             })
           }
         this.load()
         })
    }
  }
}
</script>
