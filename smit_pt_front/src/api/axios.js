import axios from 'axios';

const axios_instance = axios.create({baseURL: '/api/'});

export default axios_instance