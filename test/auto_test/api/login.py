#导包
import requests
import config
#创建接口类
class LoginAPI:
    #初始化
    def __init__(self):
        #指定url基本信息
        self.url_login = config.BASE_URL + 'login/userlogin'
    #登录
    def login(self, test_data):
        return requests.post(url=self.url_login, json=test_data)
class forgetpwdAPI:
    #初始化
    def __init__(self):
        #指定url基本信息
        self.url_forgetpwd = config.BASE_URL + 'login/forgetpwd'
    #忘记密码
    def forgetpwd(self, test_data):
        return requests.post(url=self.url_forgetpwd, json=test_data)
class insertUserAPI:
    #初始化
    def __init__(self):
        #指定url基本信息
        self.url_insertUser = config.BASE_URL + 'login/insertUser'
    #新增用户
    def insertUser(self, test_data):
        return requests.post(url=self.url_insertUser, json=test_data)
