import axios from "axios";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ||
  "http://localhost:8080/api";

const employeeApi = axios.create({
  baseURL: `${API_BASE_URL}/employees`,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
});

export const getEmployees = async ({
  page = 0,
  size = 5,
  sortBy = "id",
  direction = "asc",
} = {}) => {
  const response = await employeeApi.get("", {
    params: {
      page,
      size,
      sortBy,
      direction,
    },
  });

  return response.data;
};

export const searchEmployees = async ({
  keyword,
  page = 0,
  size = 5,
  sortBy = "firstName",
  direction = "asc",
}) => {
  const response = await employeeApi.get("/search", {
    params: {
      keyword,
      page,
      size,
      sortBy,
      direction,
    },
  });

  return response.data;
};

export const getEmployeesByDepartment = async ({
  department,
  page = 0,
  size = 5,
}) => {
  const response = await employeeApi.get("/department", {
    params: {
      name: department,
      page,
      size,
    },
  });

  return response.data;
};

export const getEmployeesByStatus = async ({
  active,
  page = 0,
  size = 5,
}) => {
  const response = await employeeApi.get("/status", {
    params: {
      active,
      page,
      size,
    },
  });

  return response.data;
};

export const getEmployeeById = async (id) => {
  const response = await employeeApi.get(`/${id}`);
  return response.data;
};

export const createEmployee = async (employeeData) => {
  const response = await employeeApi.post(
    "",
    employeeData
  );

  return response.data;
};

export const updateEmployee = async (
  id,
  employeeData
) => {
  const response = await employeeApi.put(
    `/${id}`,
    employeeData
  );

  return response.data;
};

export const deleteEmployee = async (id) => {
  await employeeApi.delete(`/${id}`);
};

export default employeeApi;