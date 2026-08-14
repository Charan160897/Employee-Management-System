import axios from "axios";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ||
  "http://localhost:8080/api";

const auditApi = axios.create({
  baseURL: `${API_BASE_URL}/audit`,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
});

auditApi.interceptors.request.use(
  (config) => {
    const storedUser =
      localStorage.getItem(
        "emsCurrentUser"
      );

    if (storedUser) {
      const user =
        JSON.parse(storedUser);

      if (user.token) {
        config.headers.Authorization =
          `Bearer ${user.token}`;
      }
    }

    return config;
  },

  (error) =>
    Promise.reject(error)
);

auditApi.interceptors.response.use(
  (response) => response,

  (error) => {
    if (
      error.response?.status === 401
    ) {
      localStorage.removeItem(
        "emsCurrentUser"
      );

      window.location.reload();
    }

    return Promise.reject(error);
  }
);

export const getAuditLogs =
  async () => {

    const response =
      await auditApi.get("");

    return response.data;
  };

export default auditApi;