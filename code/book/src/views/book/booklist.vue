<template>
    <div>
      <el-row :gutter="10">
        <el-col :span="4">
          <el-input id="searchname" placeholder="请输入图书名称" v-model="seabookname" style="width: 180px;"></el-input>
        </el-col>
        <el-col :span="4">
          <el-input id="searchauthor" placeholder="请输入作者" v-model="seaauthor" style="width: 180px;"></el-input>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="search">搜索</el-button>
        </el-col>
      </el-row>
      <el-table :data="books" stripe>
        <el-table-column prop="id" label="图书编号"></el-table-column>
        <el-table-column prop="bookname" label="图书名称"></el-table-column>
        <el-table-column prop="bookbrief" label="图书简介"></el-table-column>
        <el-table-column prop="author" label="作者"></el-table-column>
        <el-table-column prop="filename" label="文件名称">
        </el-table-column>
        <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="success" icon="el-icon-download" @click="downloadFile(scope.row)" circle></el-button>
              <el-button type="primary" icon="el-icon-edit" @click="editBook(scope.row)" circle></el-button>
              <el-button type="danger" icon="el-icon-delete" @click="deleteBook(scope.row)" circle></el-button>
            </template>
        </el-table-column>
        <a ref="fileInput" href="#" style="display:none;" :download="filename"></a>
      </el-table>
      <el-dialog :visible.sync="bookEditDialog" :before-close="bookhandleClose"  :close-on-click-modal ="false">
          <el-form :model="bookForm" :rules="rules" ref="bookForm" label-width="120px">
            <el-form-item label="图书名称" prop="bookname">
              <el-input id="bookname" type="text" v-model="bookForm.bookname" auto-complete="off" placeholder="请输入图书名称"></el-input>
            </el-form-item>
            <el-form-item label="图书简介" prop="bookbrief">
              <el-input id="bookbrief" type="text" v-model="bookForm.bookbrief" auto-complete="off" placeholder="请输入图书简介"></el-input>
            </el-form-item>
            <el-form-item label="作者" prop="author">
              <el-input id="author" type="text" v-model="bookForm.author" auto-complete="off" placeholder="请输入作者"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="bookEditDialog=false">取 消</el-button>
            <el-button type="primary" @click="saveChanges">确 定</el-button>
          </div>
      </el-dialog>
      <el-pagination :current-page="currentPage" :page-size="pageSize" :total="total"
                    @current-change="handlePageChange"></el-pagination>
  </div>
</template>

<script>
const api_name='/ap/book';
export default {
    name: "booklist",
    data() {
    return {
      books: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      bookEditDialog: false,
      filename: '',
      fileUrl: '',
      fileId: '',
      seabookname:'',
      seaauthor:'',
      bookForm: {
        id: '',
        bookname: '',
        bookbrief: '',
        author:'',
      },rules: {
        bookname: [
          { required: true, message: '请输入图书名称', trigger: 'blur' }
        ],
        bookbrief: [
          { required: true, message: '请输入图书简介', trigger: 'blur' }
        ],
        author: [
          { required: true, message: '请输入作者', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.getBook()
  },
  methods: {
   
    getBook() {
      console.log(`${api_name}/page`),
      this.$axios( {
        method:"get",
        url: `${api_name}/page`,
        params: {
          page: this.currentPage - 1,
          size: this.pageSize
        }
      }).then(response => {
        this.books = response.data.data.content
        this.total = response.data.data.totalElements
      })
    },
    editBook(row) {
      this.bookForm.id=row.id;
      this.bookForm.bookname = row.bookname; 
      this.bookForm.bookbrief = row.bookbrief; 
      this.bookForm.author = row.author; 
      console.log(this.bookForm);
      this.bookEditDialog = true// 显示编辑框
    
    },
    bookhandleClose(done) {
      // 保存数据并关闭对话框
        done();
    },
    deleteBook(book) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios({
            method:"delete",
            url: `${api_name}/deletebook/`,
            data:{
              id:book.id,
              fileId:book.fileId
            }
          }).then(response => {
            if(response.data.code==200){
                this.$message({
                  message: '删除成功',
                  type: 'success'
                });
              }else {
                this.$message.error('修改失败');
              }
            this.getBook()
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
      this.getBook()
    },
    saveChanges() {
      // 判断表单数据是否有效
       this.$axios({
          method:"Put",
          url: `${api_name}/updatebook`,
          headers: {  'Content-Type': 'application/json','charset':'UTF-8'},
          data: this.bookForm
        }).then((res)=>{
          console.log(res)
          if(res.data.code==200){
            this.$message({
              message: '修改成功',
              type: 'success'
             });
            
          }else {
            this.$message.error('修改失败');
          }
          this.bookEditDialog = false;
          this.$refs.bookForm.resetFields();
          this.getBook()
        }).catch(
            err => console.log(err))
    },
    objectIdToString(objectId) {
      if (typeof objectId === 'object') {
        return JSON.stringify(objectId);
      }
      return objectId;
    },
    downloadFile(row) {
      this.$axios({
        method:'POST',
        url: `${api_name}/downloadBook`,
        params: {
          filename: row.filename,
          fileId: row.fileId,
        },
        responseType: 'arraybuffer',
      }).then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', row.filename);
        document.body.appendChild(link);
        link.click();
      });
    },
    search() {
      this.$axios({
        method:"post",
        url:`${api_name}/searchbook`,
        headers: {  'Content-Type': 'application/json','charset':'UTF-8'},
        params: { 
          bookname: this.seabookname,
          author: this.seaauthor,
          page: this.currentPage - 1,
          size: this.pageSize
        } 
      }).then((response) => {
        this.books = response.data.content
        this.total = response.data.totalElements
      })
    },
    
  },
}
</script>

<style>

</style>