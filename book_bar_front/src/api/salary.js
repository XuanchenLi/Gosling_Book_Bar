import http from '@/utils/http'
let request = "/testIp/"

export function getPersonalSalaryAPI() {
  return http.get(`${request}/salary/personal`)
}

export function getSalaryRecordAPI(data) {
  return http.post(`${request}/salary/personal_history`, data)
}

export function getSalaryByEmployeeIdAPI(param) {
  return http.get(`${request}/salary/${param}`)
}

export function setSalaryAPI(data) {
  return http.post(`${request}/salary/save_or_update`, data)
}
