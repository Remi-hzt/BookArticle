import axios from 'axios'
import { Message } from 'element-ui'
//创建axios实例
const service = axios.create({
    baseURL: "http://localhost:6161",//后端接口地址前缀
    timeout: 5000 //请求超时时间
})
// response 拦截器
service.interceptors.response.use(//统一返回错误处理
  success=>{
    //success.status是http返回的响应码，而success.data.status才是服务器真正返回的json
    if (success.status && success.status == 200 && success.data.status == 500) {
      Message.error({message: success.data.msg});
      return;
    }
    if (success.data.msg){
      Message.success({message:success.data.msg})
    }
    return  success.data;
  },error => {
    if (error.response.status == 504 || error.response.status == 404) {
      Message.error({message: '服务器发生问题'})
    } else if (error.response.status == 403) {
      Message.error({message: '权限不足，请联系管理员'})
    } else if (error.response.status == 401) {
      Message.error({message: '尚未登录，请登录'})
    }else {
      if (error.response.data.msg){
        Message.error({message:error.response.data.msg})
      }else {
        Message.error({message:'未知错误！'})
      }
    }
    return;
  }
)

// let base='';//加前缀直接修改base就够了
// export const postKeyValueRequest=(url,params)=>{
//   return axios({
//     method:'post',
//     url: `${base}${url}`,
//     data:params,
//     transformRequest:[function (data){
//       let ret = '';
//       for(let i in data){
//         ret+=encodeURIComponent(i)+'='+encodeURIComponent(data[i])+'&'
//       }
//       console.log(ret);
//       return ret;
//     }],
//     headers:{
//       'Content-Type':'application/x-www-form-urlencoded'
//     }
//   });
// }

