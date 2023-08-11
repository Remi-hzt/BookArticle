<template>
  <div>
    <el-row :gutter="8">
        <el-col :span="4">
          <el-input placeholder="请输入用户" v-model="seaname" style="width: 180px;"></el-input>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="search">搜索</el-button>
        </el-col>
    </el-row>
    <el-table :data="users" stripe>
      <el-table-column prop="username" label="姓名"></el-table-column>
      <el-table-column prop="password" label="密码"></el-table-column>
      <el-table-column prop="role" label="权限"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit"  @click="editUser(scope.row)" circle></el-button>
          <el-button type="danger" icon="el-icon-delete"  @click="deleteUser(scope.row)" circle></el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="showUserDialog" :before-close="userhandleClose"  :close-on-click-modal ="false">
      <el-form :model="userData" :rules="rules" ref="userForm" label-width="120px">
        <el-form-item label="姓名" prop="username">
          <el-input id="username" v-model="userData.username" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input id="password" v-model="userData.password"></el-input>
        </el-form-item>
        <el-form-item>
          <label>
            <input type="radio" name="option" value="ROLE_ADMIN" v-model="userData.role"> 管理员
          </label>
          <label>
            <input type="radio" name="option" value="ROLE_USER" v-model="userData.role"> 普通用户
          </label>
      </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showUserDialog=false">取 消</el-button>
        <el-button type="primary" @click="usersaveChanges">确 定</el-button>
      </div>
    </el-dialog>
    <el-pagination :current-page="currentPage" :page-size="pageSize" :total="total"
                   @current-change="handlePageChange"></el-pagination>
  </div>
</template>

<script>
const api_name='/ap/user'
export default {
  name: "userlist",
  data() {
    return {
      users: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      showUserDialog: false,
      seaname:'',
      userData: {
        username: '',
        password: '',
        role:'',
      },
      rules: {
        password: [{required: true, message: '请输入密码', trigger: 'blur'}],
      },
    }
  },
  mounted() {
    this.getUsers()
  },
  methods: {
    async getUsers() {
      this.$axios( {
        method:"post",
        url: `${api_name}/page`,
        data: {
          page: this.currentPage - 1,
          size: this.pageSize
        }
      }).then(response => {
        console.log(response.data.data.content)
        this.users = response.data.data.content
        this.total = response.data.data.totalElements
      })
    },

     editUser(row) {
      this.userData.username = row.username; 
      this.userData.password = row.password; 
      this.userData.role = row.role; 
      console.log(this.userData );
      this.showUserDialog = true// 显示编辑框

     
    },
    userhandleClose(done) {
      // 保存数据并关闭对话框
      done();

    },
    async deleteUser(user) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios({
            method:"delete",
            url: `${api_name}/deleteuser`,
            data:{username:user.username}
          }).then(response => {
            console.log(response)
            if(response.data&&response.status==200){
                this.$message({
                  message: '删除成功',
                  type: 'success'
                });
              }else {
                this.$message.error('删除失败');
              }
            this.getUsers()
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
      
    },
    handlePageChange(currentPage) {
      this.currentPage = currentPage
      this.getUsers()
    },
    async usersaveChanges() {
      // 判断表单数据是否有效
      this.$axios({
          method:"Put",
          url: `${api_name}/updateuser`,
          headers: {  'Content-Type': 'application/json','charset':'UTF-8'},
          data: this.userData
        }).then((res)=>{
          console.log(res.data)
          if(res.data.code==200){
            this.$message({
              message: '修改成功',
              type: 'success'
             });
          }else {
            this.$message.error('修改失败');
          }
          this.showUserDialog = false;
          this.$refs.userForm.resetFields();
          this.getUsers()
        }).catch(
            err => console.log(err))
    },
    search() {
      const formData = new FormData();
      formData.append('username', this.seaname);
      formData.append('page', this.currentPage - 1);
      formData.append('size', this.pageSize);

      this.$axios({
        method:"post",
        url:`${api_name}/searchuser`,
        headers: {  'Content-Type': 'application/json','charset':'UTF-8'},
        data:{
          formData:formData
        },

      }).then((response) => {
        this.users = response.data.content
        this.total = response.data.totalElements
      })
    },
  }
}
</script>
