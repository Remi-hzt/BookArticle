from anaconda_project.internal.test.multipart import MultipartEncoder

from api.book import bookAPI

import pytest
import json
import config
from common.duanyan import duanyan
import allure


# 读取json文件
def insertbook_data(json_file):
    # 定义空列表
    test_data = []
    # 打开json文件
    with open(json_file, "r", encoding='utf-8') as f:
        # 加载json文件数据
        json_data = json.load(f)
        # 循环遍历测试数据
        for case_data in json_data:
            # 转换数据格式[{},{}] ==> [(),()]
            bookname = case_data.get("bookname")
            bookbrief = case_data.get("bookbrief")
            filename = case_data.get("filename")
            author = case_data.get("author")
            file = case_data.get("file")
            status = case_data.get("status")
            code = case_data.get("code")
            message = case_data.get("message")
            test_data.append((bookname, bookbrief, filename, author, file, status, code, message))
    # 返回处理后的数据
    return test_data

def updatebook_data(json_file):
    # 定义空列表
    test_data = []
    # 打开json文件
    with open(json_file, "r", encoding='utf-8') as f:
        # 加载json文件数据
        json_data = json.load(f)
        # 循环遍历测试数据
        for case_data in json_data:
            # 转换数据格式[{},{}] ==> [(),()]
            id = case_data.get("id")
            bookname = case_data.get("bookname")
            bookbrief = case_data.get("bookbrief")
            author = case_data.get("author")
            status = case_data.get("status")
            code = case_data.get("code")
            message = case_data.get("message")
            test_data.append((id, bookname, bookbrief, author, status, code, message))
    # 返回处理后的数据
    return test_data

def delbook_data(json_file):
    # 定义空列表
    test_data = []
    # 打开json文件
    with open(json_file, "r", encoding='utf-8') as f:
        # 加载json文件数据
        json_data = json.load(f)
        # 循环遍历测试数据
        for case_data in json_data:
            # 转换数据格式[{},{}] ==> [(),()]
            id = case_data.get("id")
            fileId = case_data.get("fileId")
            status = case_data.get("status")
            code = case_data.get("code")
            message = case_data.get("message")
            test_data.append((id, fileId, status, code, message))
    # 返回处理后的数据
    return test_data

def downbook_data(json_file):
    test_data=[]
    with open(json_file, "r", encoding='utf-8') as f:
        json_data = json.load(f)
        for case_data in json_data:
            filename = case_data.get("filename")
            fileId = case_data.get("fileId")
            status = case_data.get("status")
            test_data.append((filename, fileId, status))
    return test_data

@allure.feature("图书管理")
class TestBook:
    # 前置处理
    def setup(self):
        # 实例化接口对象
        self.book_api = bookAPI()

    # 后置处理
    def teardown(self):
        pass

    # 新增图书
    @pytest.mark.parametrize("bookname, bookbrief, author, filename, file, status, code, message",
                             insertbook_data(json_file=config.BASE_PATH + "/data/insertbook.json"))
    @allure.story('新增图书')
    def test_addbook(self, bookname, bookbrief, author, filename, file, status, code, message):

        data = {
            "bookname": bookname,
            "bookbrief": bookbrief,
            "author": author,
            "filename": filename,
        }

        # 发起文件上传请求
        files = {
             "file": open(file, "rb")
        }
        res_l = self.book_api.insertbook(data=data, files=files)
        bookid = res_l.json()
        # 检查响应中的id字段
        assert 'id' in bookid['data']
        global book_id, file_id
        book_id = bookid['data']['id']
        file_id= bookid['data']['fileId']
        print(book_id)
        print(file_id)

        # 断言
        duanyan(res_l, status, message, code)


    #修改图书
    @pytest.mark.parametrize("id, bookname, bookbrief, author, status, code, message",
                             updatebook_data(json_file=config.BASE_PATH + "/data/updatebook.json"))
    @allure.story('修改图书')
    def test_updatebook(self, id, bookname, bookbrief, author,  status, code, message):
        data = {
            "id": id,
            "bookname": bookname,
            "bookbrief": bookbrief,
            "author": author,
        }
        res_l = self.book_api.updatebook(data=data)

        # 断言
        duanyan(res_l, status, message, code)


    #下载图书
    @pytest.mark.parametrize("filename, fileId, status", downbook_data(json_file=config.BASE_PATH + "/data/downbook.json"))
    @allure.story('下载图书')
    def test_downbook(self, filename, fileId, status):
        params = {
            "filename": filename,
            "fileId": fileId
        }
        res_l = self.book_api.downbook(params=params)
        # 断言
        # 断言响应状态码
        assert status == res_l.status_code


    #删除
    @pytest.mark.parametrize("id, fileId,  status, code, message",
                             delbook_data(json_file=config.BASE_PATH + "/data/delbook.json"))
    @allure.story('删除图书')
    def test_delbook(self, id, fileId, status, code, message):
        data = {
            "id": id,
            "fileId": fileId
        }
        res_l = self.book_api.delbook(data=data)
        # 断言
        duanyan(res_l, status, message, code)