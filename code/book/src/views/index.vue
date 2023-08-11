<template>
  <el-container>
    <!-- 顶部导航栏 -->
  <el-header style="text-align: right; font-size: 12px">
    <el-dropdown>
        <i class="el-icon-setting" style="margin-right: 15px"></i>
          <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="loginout()">注销</el-dropdown-item>
              <el-dropdown-item>功能二</el-dropdown-item>
              <el-dropdown-item>功能三</el-dropdown-item>
          </el-dropdown-menu>
    </el-dropdown>
    <span>{{ showUsername }}</span>
  </el-header>
  <el-container>
    <!--左侧导航栏--> 
    <el-aside width="200px">
      <el-menu id="leftmeun"
        default-active="activeIndex" 
        class="el-menu-vertical-demo"
        @select="handleMenuSelect" 
        @open="handleOpen" @close="handleClose">
        <el-menu-item index="1" >
          <span slot="title">主页</span>
        </el-menu-item>
        <el-submenu index="2">
          <template slot="title">
            <span>文章管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item id="novellist" index="/admin/novellist">文章列表</el-menu-item>
            <el-menu-item id="addnovel" index="/admin/addnovel">新增文章</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="3">
          <template slot="title">
            <span>图书管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item id="booklist" index="/admin/booklist">图书列表</el-menu-item>
            <el-menu-item id="addbook" index="/admin/addbook">新增图书</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="4">
          <template slot="title">
            <span>用户管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item id="userlist" index="/admin/userlist">用户列表</el-menu-item>
            <el-menu-item id="adduser" index="/admin/adduser">新增用户</el-menu-item>
          </el-menu-item-group>
        </el-submenu >
      </el-menu>
    </el-aside>
    <!-- 右区域内容页面的切换 -->
    <el-main>
      <!-- 展示上面切换的组件 -->
      <router-view></router-view>
    </el-main>
  </el-container>
</el-container>
</template>

<script>
 import jsCookie from 'js-cookie'
export default {
  name: "index",
  data() {
      return {
        activeIndex: '/Home',
      };
    },
  methods: {
    handleMenuSelect(key,keyPath) {
      console.log(key,keyPath);
      this.$router.push(key);
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
.el-aside::-webkit-scrollbar {
      display: none;
   }
</style>