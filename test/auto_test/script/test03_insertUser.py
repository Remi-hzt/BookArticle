# 导包
from api.login import insertUserAPI
import pytest
import json
import config
import allure

# 读取json文件
def build_data(json_file):
    # 定义空列表
    test_data = []
    # 打开json文件
    with open(json_file, "r", encoding='utf-8') as f:
        # 加载json文件数据
        json_data = json.load(f)
        # 循环遍历测试数据
        for case_data in json_data:
            # 转换数据格式[{},{}] ==> [(),()]
            username = case_data.get("username")
            password = case_data.get("password")
            role = case_data.get("role")
            status = case_data.get("status")
            code = case_data.get("code")
            message = case_data.get("message")
            test_data.append((username, password, role, status, code, message))
    # 返回处理后的数据
    return test_data

@allure.feature("用户管理")
# 创建测试类
class TestInsertUser:
    # 前置处理
    def setup(self):
        # 实例化接口对象
        self.insertUser_api = insertUserAPI()

    # 后置处理
    def teardown(self):
        pass

    # 用户新增
    @pytest.mark.parametrize("username, password, role, status, code, message",
                             build_data(json_file=config.BASE_PATH + "/data/insertUser.json"))
    @allure.story('用户新增')
    def test_insertUser(self, username, password, role, status, code, message):
        # 登录
        login_data = {
            "username": username,
            "password": password,
            "role": role,
            "status": status,
            "code": code,
            "message": message
        }
        res_l = self.insertUser_api.insertUser(test_data=login_data)
        # 断言响应状态码为200
        assert status == res_l.status_code
        # 断言响应数据包含成功
        assert message in res_l.text
        # 断言响应json数据中code值
        assert code == res_l.json().get("code")
        print(res_l.json())
