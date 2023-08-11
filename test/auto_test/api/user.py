#导包
import requests
import config
#创建接口类
class UpdateUserAPI:
    #初始化
    def __init__(self):
        #指定url基本信息
        self.url_updateuser = config.BASE_URL + config.user_URL + '/updateuser'

    #登录
    def updateuser(self, test_data):
        return requests.put(url=self.url_updateuser, json=test_data)