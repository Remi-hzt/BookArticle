<template>
    <div class="loginContainer">
      <el-form :rules="rules" :model="loginForm" ref="loginform" :inline="true" label-width="100px">
        <h3>系统登录</h3>
        <el-form-item label="用户名" prop="username">
          <el-input id="username" type="text" v-model="loginForm.username" auto-complete="off" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密 码" prop="password">
          <el-input id="password" type="password" v-model="loginForm.password" auto-complete="off" placeholder="请输入密码"></el-input>
        </el-form-item>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-checkbox v-model="loginForm.rememberMe" class="loginRememberMe" style="width: 100%">记住密码</el-checkbox>
            </el-col>
            <el-col :span="6" :offset="6">
              <el-link type="primary" style="width: 100%"  @click="forgetpwd">忘记密码</el-link>
            </el-col>
          </el-row>           
          <el-button type="primary" style="width: 45%;margin-top: 10px" @click="loginSubmit">登录</el-button>
          <el-link type="primary" style="width: 25%;margin-left: 10px"  @click="register">注册</el-link>
      </el-form>
  
    </div>
  </template>
  
  <script>
  import jsCookie from 'js-cookie'
  const api_name='/login'
  export default {
  name: "login",
    data(){
    return {
      loginForm:{
        username:'',
        password:'',
        rememberMe:false,
      },
  
      rules: {
        username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        password: [{required: true, message: '请输入密码', trigger: 'blur'}],
      },
  
    }
    },
    methods:{
      loginSubmit(){
        let user = this.loginForm//将数据写入变量
        this.$axios({
          method:"post",
          url: `${api_name}/userlogin`,
          headers: {  'Content-Type': 'application/json','charset':'UTF-8'},
          data: user
        }).then((res)=>{
          console.log(res.data)
  
          if(res.data.code==200){
            let username=res.data.data.user.username
            let jwtToken=res.data.data.jwtToken
            let role=res.data.data.user.role
            jsCookie.set('username',username)
            localStorage.setItem('jwt', jwtToken)
            console.log(localStorage.jwtToken)
            if(role=="ROLE_ADMIN"){
              // 将 JWT Token 存储在 localStorage 中
              this.$router.push({ path:'/admin/index'})
            }else{
              // 将 JWT Token 存储在 localStorage 中
              localStorage.setItem('jwt', jwtToken)
              this.$router.push({ path:'/user/index'})
            }
            // 重新加载页面
           // location.reload()
          }else{
            this.$message.error('用户名不存在或密码错误')
          }
          this.loginForm={}
          // this.resetForm('loginform')
        }).catch(err =>{
          console.log(err)
          // if(err.response.status==401){
             this.$message.error('操作错误');
          // }
        })
      },
      register(){
        this.$router.push({ path:'/register'  })
      },
      forgetpwd(){
        this.$router.push({ path:'/forgetpwd'  })
      }

    },
   
}
  </script>
  
  <style>
    .loginContainer {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      /* 垂直和水平居中 */
      border-radius: 15px;
      background-clip: padding-box;
      width: 350px;
      border: 1px solid #ffffff;
      padding: 15px 75px 35px 75px;
      background: #ffffff;
      box-shadow: 0 0 20px #2c3e50;
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
    }
    .loginRememberMe{
      text-align: left;
    }
    .el-link:hover {
      text-decoration: none;
    }
  </style>
