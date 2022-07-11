import axios from "axios"

const base = "http://localhost:8080/api"
const storage = JSON.parse(sessionStorage.getItem("data"))

// eslint-disable-next-line no-return-await,no-unused-vars
export const login = (payload) => {
    return axios.post(`${base}/auth/${payload.role}/login`, payload)
}
export const verify = (payload) => {
    return axios.post(`${base}/user/${payload.user}/confirmaccount`, payload.tok, { headers: { "Content-Type": "text/plain" } })
}
export const choosePassword = (payload) => {
    const obj = {
        email: payload.user,
        newpassword: payload.password
    }
    return axios.post(`${base}/user/confirmaccount/password`, obj)
}
export const resetPassword = (id) => {
    return axios.post(`${base}/user/password/reset`, id, { headers: { "Content-Type": "text/plain" } })
}
export const changePassword = (payload) => {
    return axios.post(`${base}/user/password/change`, payload)
}
export const register = (payload) => {
    return axios.post(`${base}/user`, payload)
}
export const findActivePolls = (email) => {
    return axios.get(`${base}/poll/user/${email}/active`)
}
export const findInActivePolls = (email) => {
    return axios.get(`${base}/poll/user/${email}/inactive`)
}
export const resendToken = (email) => {
    const config = {
        headers: {
            'Authorization': `Bearer ${(JSON.parse(sessionStorage.getItem("data")).auth)}`
        }}
    return axios.get(`${base}/user/${email}/token/send`,config)

}
export const voteNow = (payload) => {
    const config = {
        headers: {
            'Authorization': `Bearer ${(JSON.parse(sessionStorage.getItem("data")).auth)}`
        }}
    return axios.post(`${base}/poll/vote`, payload,config)

}
export const getOpenPolls = (type) => {
    return axios.get(`${base}/poll/user/email/${type}`)
}
export const createAndEditPoll=(payload)=>{
    const config = {
        headers: {
            'Authorization': `${(JSON.parse(sessionStorage.getItem("data")).auth)}`
        }}
    return axios.post(`${base}/poll`, payload,config)
}
export const addAndEditCandidate=(payload)=>{
    const str=payload.fullname.split(" ")
    var obj={
        poll:payload.poll.value,
        firstname:str[1],
        lastname:str[0],
        othername:str[2]!==null?str[2]:"",
        imageSrc:payload.imageSrc
    }
    const config = {
        headers: {
            'Authorization': `${(JSON.parse(sessionStorage.getItem("data")).auth)}`
        }}
    return axios.post(`${base}/poll/${obj.poll}/add`, obj,config)
    
}
export const getUsers=(type)=>{
    const config = {
        headers: {
            'Authorization': `${(JSON.parse(sessionStorage.getItem("data")).auth)}`
        }}
    return axios.get(`${base}/user/${type}`,config)
}
export const enrollUser=(payload)=>{
    const config = {
        headers: {
            'Authorization': `${(JSON.parse(sessionStorage.getItem("data")).auth)}`
        }}
        return axios.get(`${base}/poll/${payload.pollId.value}/accredit/${payload.userId.value}`,config)
}
export const uploadUsers = (pollId,formdata) => {
    const config = {
        headers: {
            'Authorization': `${(JSON.parse(sessionStorage.getItem("data")).auth)}`
        }
    }
    return axios.post(`${base}/poll/${pollId}/accredit`, formdata, config)
}
export const downloadUsers = () => {
    const config = {
        headers: {
            'Authorization': `${(JSON.parse(sessionStorage.getItem("data")).auth)}`,
            responseType:'blob'
        }
    }
    return axios.put(`${base}/user/csv`, null,config)
}
export const toggleBlock=(email)=>{
    const config = {
        headers: {
            'Authorization': `${(JSON.parse(sessionStorage.getItem("data")).auth)}`
        }
    }
    return axios.get(`${base}/user/${email}/toggleblock`, config)
}