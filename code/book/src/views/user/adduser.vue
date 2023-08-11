<template>
  <div class="index">
    <el-form :model="userForm" label-width="80px">
      <el-form-item label="用 户 名" prop="username">
        <el-input id="username" type="text" v-model="userForm.username" auto-complete="off" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密   码" prop="password">
        <el-input id="password" type="password" v-model="userForm.password" auto-complete="off" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item>
          <label>
            <input type="radio" name="option" value="ROLE_ADMIN" v-model="userForm.role"> 管理员
          </label>
          <label>
            <input type="radio" name="option" value="ROLE_USER" v-model="userForm.role"> 普通用户
          </label>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addUser">添加用户</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
const api_name='/ap/login'
export default {
    name: "adduser",
    data(){
    return {
      userForm:{
        username:'',
        password:'',
        role: '', // 用于存储用户选择的选项
      },
      checked:true,
      rules: {
        username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        password: [{required: true, message: '请输入密码', trigger: 'blur'}],
      },
  
    }
  },
  mounted() {
  },
  methods: {
    addUser() {
      let user = this.userForm//将数据写入变量
        console.log(user)
        this.$axios({
          method:"post",
          url: `/login/insertUser`,
          headers: {  'Content-Type': 'application/json','charset':'UTF-8'},
          data: user
        }).then((res)=>{
          console.log(res)
          if(res.data.code==200){
            this.$message({
              message: '新增成功',
              type: 'success'
             });
             
          }
          else if (res.data.message=="用户名存在") {
            this.$message.error('用户名存在');
          } else {
            this.$message.error('新增失败');
          }
          this.userForm={}
          // this.resetForm('loginform')
        }).catch(
        err => console.log(err))
    }
   
  }
}
</script>

<style>

</style>