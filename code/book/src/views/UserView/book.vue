<template>
    <div class="userindex">
        <el-table :data="books" stripe>
        <el-table-column prop="bookname" label="图书名称"></el-table-column>
        <el-table-column prop="bookbrief" label="图书简介"></el-table-column>
        <el-table-column prop="author" label="作者"></el-table-column>
        <el-table-column prop="filename" label="文件名称">
        </el-table-column>
        <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="success" icon="el-icon-download" @click="downloadFile(scope.row)" circle></el-button>
            </template>
        </el-table-column>
        <a ref="fileInput" href="#" style="display:none;" :download="filename"></a>
        </el-table>
        <el-pagination :current-page="currentPage" :page-size="pageSize" :total="total"
                    @current-change="handlePageChange"></el-pagination>
    </div>
  </template>
  
  <script>
  const api_name='/ap/book'
  export default {
    name: "book",
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
        }
    },
    mounted() {
        this.getBook()
    },
    methods: {
        getBook() {
        this.$axios( {
            method:"get",
            url: `${api_name}/page`,
            params: {
            page: this.currentPage - 1,
            size: this.pageSize
            }
        }).then(response => {
            this.books = response.data.content
            this.total = response.data.totalElements
            console.log(this.books)
        })
        },
        handlePageChange(currentPage) {
        this.currentPage = currentPage
        this.getBook()
        },
        downloadFile(row) {
        console.log(row.filename)
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
        
    },
  }
  </script>
  
  <style>
  
  </style>