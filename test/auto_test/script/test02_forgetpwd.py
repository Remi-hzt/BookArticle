# 导包
from api.login import forgetpwdAPI
import pytest
import json
import config
import allure
# 用户登录测试

# 读取json文件
def forgetpwd_data(json_file):
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
            status = case_data.get("status")
            code = case_data.get("code")
            message = case_data.get("message")
            test_data.append((username, password, status, code, message))
    # 返回处理后的数据
    return test_data

@allure.feature("用户管理")
# 创建测试类
class TestForgetPwd:
    # 前置处理
    def setup(self):
        # 实例化接口对象
        self.forgetpwd_api = forgetpwdAPI()

    # 后置处理
    def teardown(self):
        pass

    # 忘记密码
    @pytest.mark.parametrize("username, password, status, code, message",
                             forgetpwd_data(json_file=config.BASE_PATH + "/data/forgetpwd.json"))
    @allure.story('忘记密码')
    def test_forgetpwd(self, username, password, status, code, message):
        data = {
            "username": username,
            "password": password,
            "status": status,
            "code": code,
            "message": message
        }
        res_l = self.forgetpwd_api.forgetpwd(test_data=data)
        # 断言响应状态码为200
        assert status == res_l.status_code
        # 断言响应数据包含成功
        assert message in res_l.text
        # 断言响应json数据中code值
        assert code == res_l.json().get("code")
        print(res_l.json())
