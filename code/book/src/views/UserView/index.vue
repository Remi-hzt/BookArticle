<template>
  <el-container>
  <el-header style="background-color:#fff;border-bottom: solid 1px #e6e6e6;">
    <el-row :gutter="20">
        <el-col :span="20">
            <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
                <el-menu-item id="homemenu" index="/user/home">主页</el-menu-item>
                <el-menu-item id="novelmenu" index="/user/novel">文章</el-menu-item>
                <el-menu-item id="bookmenu" index="/user/book" >图书</el-menu-item>
            </el-menu>
        </el-col>
        <el-col :span="4" style="text-align: right; font-size: 12px">
            <el-dropdown>
                 <i class="el-icon-setting" style="margin-right: 15px"></i>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="editUser(showUsername)">个人管理</el-dropdown-item>
                    <el-dropdown-item @click.native="loginout()">注销</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            <span>{{ showUsername }}</span>
        </el-col>
    </el-row>
  </el-header>
  <el-dialog :visible.sync="showUserDialog" :before-close="userhandleClose"  :close-on-click-modal ="false">
      <el-form :model="userData" :rules="rules" ref="userForm" label-width="120px">
        <el-form-item label="姓名" prop="username">
          <el-input v-model="userData.username" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="旧密码" prop="ordpassword">
          <el-input v-model="userData.ordpassword"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newpassword">
          <el-input v-model="userData.newpassword"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showUserDialog=false">取 消</el-button>
        <el-button type="primary" @click="usersaveChanges">确 定</el-button>
      </div>
    </el-dialog>
  <el-main> 
    <router-view></router-view>
  </el-main>
</el-container>
</template>

<script>
const api_name='/ap/user'
 import jsCookie from 'js-cookie'
export default {
  name: "userindex",
  data() {
      return {
        activeIndex: '/userhome',
        showUserDialog: false,
        userData: {
          username: '',
          ordpassword: '',
          newpassword: '',
        },
        rules: {
        username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        ordpassword: [{required: true, message: '请输入旧密码', trigger: 'blur'}],
        newpassword: [{required: true, message: '请输入新密码', trigger: 'blur'}],
      },
  
      };
    },
  methods: {
    handleSelect(key,keyPath) {
      console.log(key,keyPath);
      this.$router.push(key);
    },
    editUser(username) {
      console.log(username);
      this.userData.username = username; 
      //this.userData.password = row.password; 
      console.log(this.userData);
      this.showUserDialog = true// 显示编辑框 
    },
    async usersaveChanges() {
      // 判断表单数据是否有效
      this.$axios({
          method:"post",
          url: `${api_name}/updatepwd`,
          headers: {  'Content-Type': 'application/json','charset':'UTF-8'},
          params: {
            username:this.userData.username,
            ordpassword:this.userData.ordpassword,
            newpassword:this.userData.newpassword
          }
        }).then((res)=>{
          console.log(res)
          if(res.data=="SUCCESS"&&res.status==200){
            this.$message({
              message: '修改成功',
              type: 'success'
             });
          }else if (res.data=="旧密码错误"&&res.status==200) {
            this.$message.error('旧密码错误');
          } 
          else {
            this.$message.error('修改失败');
          }
          this.showUserDialog = false;
          this.$refs.userForm.resetFields();
          this.getUsers()
        }).catch(
            err => console.log(err))
    },
    userhandleClose(done) {
      // 保存数据并关闭对话框
      done();

    },

    loginout() {
      console.log("注销")
        this.$axios.get('/login/logout')
        .then(response => {
           if(response.status==200){
             // 清空本地存储
              localStorage.removeItem('jwt')
              jsCookie.remove('username')
              // 重定向到登录页面
              this.$router.push('/login')
           }
        })
        .catch(error => {
           console.log(error);
        });
    }
  },
  computed:{
    showUsername(){
      return jsCookie.get('username')
    }
  }
}
</script>

<style>

</style>