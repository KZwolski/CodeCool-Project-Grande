import axios from "axios";
import {useNavigate} from "react-router-dom";

const API_URL = "http://localhost:3000/api/auth/";


const register = (username, password) => {
    let customConfig = {
        headers: {
            'Content-Type': 'application/json'
        }
    };
    let json = JSON.stringify({
        username: username,
        password: password,
    })
    return axios.post(API_URL + "signup", json, customConfig)
};

const login = (email, password, token) => {
    let customConfig = {
        headers: {
            'Content-Type': 'application/json',
            "Access-Control-Allow-Origin": "*",
        }
    };
    let json = JSON.stringify({
        email: email,
        password: password,
        token: token
    })
    return axios
        .post(API_URL + "login", json, customConfig).then((response) => {
            console.log("response axiosa po wyslanym poscie: " + JSON.stringify(response.data))
            localStorage.setItem("user", JSON.stringify(response.data))
            const user = JSON.parse(localStorage.getItem('user'))
        })
}

const parseJwt = (token) => {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace('-', '+').replace('_', '/');
    console.log('return: ' + window.atob(base64))
    return JSON.parse(window.atob(base64));
}

const loginOauthUser = (email, name, password) => {
    let customConfig = {
        headers: {
            'Content-Type': 'application/json',
            "Access-Control-Allow-Origin": "*",
        }
    };
    let json = JSON.stringify({
        email: email,
        username: name,
        password: password,
    })
    return axios
        .post(API_URL + "login/oauth", json, customConfig).then((response) => {
            localStorage.setItem("user", JSON.stringify(response.data))
            const user = JSON.parse(localStorage.getItem('user'))
        })
}

const getCurrentUser = () => {
    return JSON.parse(localStorage.getItem('user'));
}


const logout = () => {
    return axios.post(API_URL + "logout").then((response) => {
        localStorage.removeItem("user");
        localStorage.clear();
        useNavigate("/home");
        return response.data;
    });
};


const AuthService = {
    register,
    login,
    logout,
    getCurrentUser,
    parseJwt,
    loginOauthUser
}

export default AuthService;
