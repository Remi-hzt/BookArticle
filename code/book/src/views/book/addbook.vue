<template>
    <div class="index">
        <el-form :model="bookForm" label-width="80px">
        <el-form-item label="图书名称" prop="bookname">
            <el-input id="bookname" type="text" v-model="bookForm.bookname" auto-complete="off" placeholder="请输入图书名称"></el-input>
        </el-form-item>
        <el-form-item label="图书简介" prop="bookbrief">
            <el-input id="bookbrief" type="text" v-model="bookForm.bookbrief" auto-complete="off" placeholder="请输入图书简介"></el-input>
        </el-form-item>
        <el-form-item label="作者" prop="author">
            <el-input id="author" type="text" v-model="bookForm.author" auto-complete="off" placeholder="请输入作者"></el-input>
        </el-form-item>
        <el-form-item label="文件" prop="file"> 
          <el-upload
          class="upload-demo"
          :before-upload="BeforeUpload"
          :limit="1"
          :accept="'.pdf, .txt'"
          :show-file-list="false"
          >
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <div slot="tip" class="el-upload__tip">只能上传 txt/pdf 文件</div>
          </el-upload>
          <div v-if="uploadFile">
            {{ uploadFile.name }}
            <el-button type="danger" size="small" @click="removeUploadFile">移除</el-button>
          </div>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="addBook">添加图书</el-button>
        </el-form-item>
        </el-form>
    </div>
</template>

<script>
import { Message, Loading } from 'element-ui';
import axios from 'axios';

let loadingInstance;

axios.interceptors.request.use(config => {
  // 显示 Loading
  loadingInstance = Loading.service();
  return config;
}, error => {
  loadingInstance = Loading.service();
  return Promise.reject(error);
});

axios.interceptors.response.use(response => {
  // 隐藏 Loading
  loadingInstance && loadingInstance.close();
  return response;
}, error => {
  loadingInstance && loadingInstance.close();
  return Promise.reject(error);
});
const api_name='/ap/book'
export default {
    name: "addbook",
    data(){
    return {
      bookForm:{
        bookname:'',
        bookbrief:'',
        author:'',
        filename:'',
        file: null
       },
      file:null,
      uploadFile: null, // 上传文件信息
      checked:true,
      rules: {
        bookname: [{required: true, message: '请输入小说名称', trigger: 'blur'}],
        bookbrief: [{required: true, message: '请输入小说简介', trigger: 'blur'}],
        author: [{required: true, message: '请输入作者', trigger: 'blur'}],
      },
    }
  },
  mounted() {
  },
  methods: {
    addBook() {
      if (this.uploadFile) {
        this.file= this.uploadFile.file
        this.bookForm.filename= this.uploadFile.name;
      }
      let bookfile=this.file
      // 添加文件
      this.bookForm.file=bookfile
      console.log(this.bookForm)

      this.$axios({
        method:"post",
        url: `${api_name}/insertbook`,
        headers: {
          'Content-Type': 'multipart/form-data','charset':'UTF-8'
        },
        data:{
          bookname:this.bookForm.bookname,
          bookbrief:this.bookForm.bookbrief,
          author:this.bookForm.author,
          filename:this.bookForm.filename,
          file:this.bookForm.file
        }
        
      }).then((res)=>{
        console.log(res)
        if(res.data.code==200){
          this.$message({
            message: '新增成功',
            type: 'success'
          });
        }
        else {
          this.$message.error('新增失败');
        }
        this.bookForm={}
        this.removeUploadFile()
          // this.resetForm('loginform')
      }).catch(
        err => console.log(err))
    },
    //验证文件类型和存储文件信息
    BeforeUpload(file) {
      const isPDFOrTXT = file.type === 'application/pdf' || file.type === 'text/plain';
      if (!isPDFOrTXT) {
        this.$message.error('上传文件格式必须为 txt 或 pdf');
      } else {
        //文件信息
        this.uploadFile = { name: file.name, file };
      }
      //阻止上传到服务器
      return false;
    },
    removeUploadFile() {
      this.uploadFile = null;
    },
  
    handleSuccess(response) {
      console.log(response);
    },
    handleError(error) {
      console.log(error);
    }
  }
}
</script>

<style>

</style>