import http from '@/utils/http'
let request = "/testIp/"

export function requestEmployeeDateAPI(data) {
  return http.post(`${request}/search/employee`, data)
}

export function getEmployeeRecommend(data) {
  return http.post(`${request}/search/employee/recommend`, data)
}
