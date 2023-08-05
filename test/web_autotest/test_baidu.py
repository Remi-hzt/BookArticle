from selenium import webdriver
import time
driver = webdriver.Chrome(r'D:\Google\Chrome\Application\chrome.exe')
driver.get('https://www.baidu.com')
time.sleep(5)