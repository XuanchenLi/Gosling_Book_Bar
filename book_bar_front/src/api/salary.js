import http from '@/utils/http'
let request = "/testIp/"

export function getPersonalSalaryAPI() {
  return http.get(`${request}/salary/personal`)
}

export function getSalaryRecord(data) {
  return http.post(`${request}/salary/personal_history`, data)
}
