<template>
    <div style="text-align: center;">
      <el-form :rules="rules"  :model="forgetpwdForm" class="loginContainer" ref="forgetpwdForm" :inline="true"  label-width="100px">
        <h3>忘记密码</h3>
        <el-form-item label="用 户 名" prop="username">
          <el-input type="text" v-model="forgetpwdForm.username" auto-complete="off" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密   码" prop="password">
          <el-input type="password" v-model="forgetpwdForm.password" auto-complete="off" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="passwordConfirm">
          <el-input type="password" v-model="forgetpwdForm.passwordConfirm" auto-complete="off" placeholder="请输入密码"></el-input>
        </el-form-item>         
        <el-button type="primary" style="width: 45%;margin-top: 10px" @click="forget">确定</el-button>
        <el-link type="primary" style="width: 45%"  @click="login">返回登录</el-link>
        
      </el-form>
  
    </div>
  </template>
  <script>
  //import {postKeyValueRequest} from "../utils/request";
  const api_name='/login'
  export default {
  name: "forgetpwd",
    data(){
    return {
     forgetpwdForm:{
        username:'',
        password:'',
        passwordConfirm: '',
      },
      checked:true,
      
      rules: {
        username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        password: [{required: true, message: '请输入密码', trigger: 'blur'}],
        passwordConfirm: [
          { required: true, message: '确认密码不能为空', trigger: 'blur' },
          // 判断两个密码是否相同
          { validator: this.validatePassword, trigger: 'blur' }
        ]
      },
  
    }
          },
    methods:{
      //忘记密码
      forget(){
        let user = this.forgetpwdForm//将数据写入变量
        console.log(user)
        this.$axios({
          method:"post",
          url: `${api_name}/forgetpwd`,
          headers: {  'Content-Type': 'application/json','charset':'UTF-8'},
          data:{ 
            username:user.username,
            password:user.password
         }
        }).then((res)=>{
          console.log(res)
          if(res.data.code==200){
            this.$message({
              message: '修改成功',
              type: 'success'
             });
            this.$router.push({ path:'/login'  })
          }
          else if (res.data.message=="用户名不存在") {
            this.$message.error('用户名不存在');
          } else {
            this.$message.error('修改失败');
          }
          this.forgetpwdForm={}
          // this.resetForm('loginform')
        }).catch(
            err => console.log(err))
      },
      //跳转login页面
      login(){
        this.$router.push({ path:'/login'  })
      },
      //判断密码是否一致
      validatePassword(rules, value, callback) {
        // 回调函数的第三个参数为一个回调函数，可以用来设置校验结果
        if (value !== this.forgetpwdForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },

    },
   
}
  </script>
  
  <style>
    .loginContainer{
      border-radius: 15px;
      background-clip: padding-box;
      margin:auto;
      width: 350px;
      border: 1px solid #ffffff;
      padding: 15px 75px 35px 75px;
      background: #ffffff;
      box-shadow: 0 0 20px #2c3e50;
    }
  </style>
