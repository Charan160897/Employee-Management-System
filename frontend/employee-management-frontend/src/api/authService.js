import axios from "axios";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ||
  "http://localhost:8080/api";

const authApi = axios.create({
  baseURL: `${API_BASE_URL}/auth`,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
});

export const loginUser = async (
  credentials
) => {
  const response =
    await authApi.post(
      "/login",
      credentials
    );

  return response.data;
};

export default authApi;