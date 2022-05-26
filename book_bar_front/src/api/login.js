import http from '@/utils/http'
//
/**
 *  @parms resquest 请求地址 例如：http://197.82.15.15:8088/request/...
 *  @param '/testIp'代表vue-cil中config，index.js中配置的代理
 */
let request = "/testIp/"

export function loginAPI (params) {
  return http.post(`${request}/employee/login`, params)
}

export function registerAPI (params) {
  return http.post(`${request}/employee/register`, params)
}

export function logoutAPI() {
  return http.get(`${request}/employee/logout`)
}
