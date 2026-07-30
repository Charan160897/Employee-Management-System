function EmployeeList({ employees, onDelete }) {
  if (!employees || employees.length === 0) {
    return (
      <div className="empty-state">
        <h2>No employees found</h2>
        <p>Create employee records through Postman to see them here.</p>
      </div>
    );
  }

  return (
    <div className="table-container">
      <table className="employee-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Employee</th>
            <th>Department</th>
            <th>Job Title</th>
            <th>Salary</th>
            <th>Hire Date</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {employees.map((employee) => (
            <tr key={employee.id}>
              <td>{employee.id}</td>

              <td>
                <div className="employee-name">
                  {employee.fullName ||
                    `${employee.firstName} ${employee.lastName}`}
                </div>

                <div className="employee-email">
                  {employee.email}
                </div>
              </td>

              <td>{employee.department}</td>

              <td>{employee.jobTitle}</td>

              <td>
                {Number(employee.salary).toLocaleString("en-US", {
                  style: "currency",
                  currency: "USD",
                })}
              </td>

              <td>{employee.hireDate}</td>

              <td>
                <span
                  className={
                    employee.active
                      ? "status status-active"
                      : "status status-inactive"
                  }
                >
                  {employee.active ? "Active" : "Inactive"}
                </span>
              </td>

              <td>
                <button
                  type="button"
                  className="delete-button"
                  onClick={() => onDelete(employee)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default EmployeeList;