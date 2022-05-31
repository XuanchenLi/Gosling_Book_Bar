import http from '@/utils/http'
let request = "/testIp/"

export function getAllBasicByDepartmentIdAPI(params) {
  return http.get(`${request}/position/all_basic_${params}`)
}

export function getPositionInfoAPI(params) {
  return http.get(`${request}/position/get_info_${params}`)
}
export function deletePositionAPI(params) {
  return http.get(`${request}/position/delete_${params}`)
}
export function updatePositionAPI(id, data) {
  return http.post(`${request}/position/update_${id}`, data)
}

export function saveOrUpdatePositionAPI(data) {
  return http.post(`${request}/position/save_or_update`, data)
}

