def duanyan(res_l, status, message, code):
    print(res_l.json())
    # 断言响应状态码
    assert status == res_l.status_code
    # 断言响应数据
    assert message in res_l.text
    # 断言响应json数据中code值
    assert code == res_l.json().get("code")