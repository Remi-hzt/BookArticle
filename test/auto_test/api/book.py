import requests
import config

class bookAPI:
    def __init__(self):
        self.url_insertbook = config.BASE_URL + config.book_URL + '/insertbook'
        self.url_updatebook = config.BASE_URL + config.book_URL + '/updatebook'
        self.url_delbook = config.BASE_URL + config.book_URL + '/deletebook'
        self.url_downbook = config.BASE_URL + config.book_URL + '/downloadBook'

    #新增图书
    def insertbook(self, data, files):
        with requests.Session() as session:
            return session.request(method='POST', url=self.url_insertbook, data=data, files=files)

    #修改图书
    def updatebook(self, data):
        return requests.put(url=self.url_updatebook, json=data)

    #删除图书
    def delbook(self, data):
        return requests.delete(url=self.url_delbook, json=data)

    #下载图书
    def downbook(self, params):
        return requests.post(url=self.url_downbook, params=params)
