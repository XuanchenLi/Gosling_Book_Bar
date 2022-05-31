import http from '@/utils/http'
let request = "/testIp/"

export function getEmployeeInfoAPI(params) {
  return http.get(`${request}/search/employee/${params}`);
}

export function getEmployeePositionAPI(params) {
  return http.get(`${request}/employee/position_${params}`)
}

export function deleteEmployeePositionAPI(id, pId) {
  return http.get(`${request}/employee/delete_position/${id}_${pId}`)
}

export function addEmployeePositionAPI(id, pId) {
  return http.get(`${request}/employee/add_position/${id}_${pId}`)
}

export function getEmployeeAllocateHistory(id) {
  return http.get(`${request}/employee/allocate_history/${id}`)
}
