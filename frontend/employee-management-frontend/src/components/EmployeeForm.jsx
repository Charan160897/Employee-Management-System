import { useEffect, useState } from "react";

const emptyFormData = {
  firstName: "",
  lastName: "",
  email: "",
  department: "",
  jobTitle: "",
  salary: "",
  hireDate: "",
  active: true,
};

const getEmployeeFormData = (employee) => {
  if (!employee) {
    return emptyFormData;
  }

  return {
    firstName: employee.firstName || "",
    lastName: employee.lastName || "",
    email: employee.email || "",
    department: employee.department || "",
    jobTitle: employee.jobTitle || "",
    salary:
      employee.salary === null ||
      employee.salary === undefined
        ? ""
        : String(employee.salary),
    hireDate: employee.hireDate || "",
    active: employee.active ?? true,
  };
};

function EmployeeForm({
  employee,
  onSubmit,
  onCancel,
  submitting,
  serverErrors,
}) {
  const isEditMode = Boolean(employee?.id);

  const [formData, setFormData] = useState(
    getEmployeeFormData(employee)
  );

  const [clientErrors, setClientErrors] =
    useState({});

  useEffect(() => {
    setFormData(getEmployeeFormData(employee));
    setClientErrors({});
  }, [employee]);

  const validateForm = () => {
    const errors = {};

    const firstName = formData.firstName.trim();
    const lastName = formData.lastName.trim();
    const email = formData.email.trim();
    const department = formData.department.trim();
    const jobTitle = formData.jobTitle.trim();
    const salaryNumber = Number(formData.salary);

    if (!firstName) {
      errors.firstName =
        "First name is required.";
    } else if (firstName.length < 2) {
      errors.firstName =
        "First name must contain at least 2 characters.";
    } else if (firstName.length > 50) {
      errors.firstName =
        "First name cannot exceed 50 characters.";
    }

    if (!lastName) {
      errors.lastName =
        "Last name is required.";
    } else if (lastName.length < 2) {
      errors.lastName =
        "Last name must contain at least 2 characters.";
    } else if (lastName.length > 50) {
      errors.lastName =
        "Last name cannot exceed 50 characters.";
    }

    if (!email) {
      errors.email = "Email is required.";
    } else {
      const emailPattern =
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

      if (!emailPattern.test(email)) {
        errors.email =
          "Enter a valid email address.";
      }
    }

    if (!department) {
      errors.department =
        "Department is required.";
    }

    if (!jobTitle) {
      errors.jobTitle =
        "Job title is required.";
    }

    if (formData.salary === "") {
      errors.salary =
        "Salary is required.";
    } else if (Number.isNaN(salaryNumber)) {
      errors.salary =
        "Salary must be a valid number.";
    } else if (salaryNumber < 0) {
      errors.salary =
        "Salary cannot be negative.";
    }

    if (!formData.hireDate) {
      errors.hireDate =
        "Hire date is required.";
    }

    return errors;
  };

  const handleChange = (event) => {
    const {
      name,
      value,
      type,
      checked,
    } = event.target;

    setFormData((currentFormData) => ({
      ...currentFormData,
      [name]:
        type === "checkbox"
          ? checked
          : value,
    }));

    setClientErrors((currentErrors) => ({
      ...currentErrors,
      [name]: "",
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    const validationErrors = validateForm();

    if (
      Object.keys(validationErrors).length > 0
    ) {
      setClientErrors(validationErrors);
      return;
    }

    const employeeData = {
      firstName:
        formData.firstName.trim(),
      lastName:
        formData.lastName.trim(),
      email:
        formData.email.trim().toLowerCase(),
      department:
        formData.department.trim(),
      jobTitle:
        formData.jobTitle.trim(),
      salary: Number(formData.salary),
      hireDate: formData.hireDate,
      active: formData.active,
    };

    const successful =
      await onSubmit(employeeData);

    if (successful && !isEditMode) {
      setFormData(emptyFormData);
      setClientErrors({});
    }
  };

  const handleReset = () => {
    setFormData(
      getEmployeeFormData(employee)
    );

    setClientErrors({});
  };

  const getFieldError = (fieldName) => {
    return (
      clientErrors[fieldName] ||
      serverErrors?.[fieldName] ||
      ""
    );
  };

  return (
    <section className="employee-form-card">
      <div className="form-header">
        <div>
          <h2>
            {isEditMode
              ? "Edit Employee"
              : "Add Employee"}
          </h2>

          <p>
            {isEditMode
              ? `Update employee ID ${employee.id}.`
              : "Enter the employee information and save it to the database."}
          </p>
        </div>

        <button
          type="button"
          className="close-form-button"
          onClick={onCancel}
          disabled={submitting}
          aria-label="Close employee form"
        >
          ×
        </button>
      </div>

      <form
        className="employee-form"
        onSubmit={handleSubmit}
        noValidate
      >
        <div className="form-grid">
          <div className="form-field">
            <label htmlFor="firstName">
              First name <span>*</span>
            </label>

            <input
              id="firstName"
              name="firstName"
              type="text"
              value={formData.firstName}
              onChange={handleChange}
              placeholder="Enter first name"
              className={
                getFieldError("firstName")
                  ? "input-error"
                  : ""
              }
              disabled={submitting}
            />

            {getFieldError("firstName") && (
              <small className="field-error">
                {getFieldError("firstName")}
              </small>
            )}
          </div>

          <div className="form-field">
            <label htmlFor="lastName">
              Last name <span>*</span>
            </label>

            <input
              id="lastName"
              name="lastName"
              type="text"
              value={formData.lastName}
              onChange={handleChange}
              placeholder="Enter last name"
              className={
                getFieldError("lastName")
                  ? "input-error"
                  : ""
              }
              disabled={submitting}
            />

            {getFieldError("lastName") && (
              <small className="field-error">
                {getFieldError("lastName")}
              </small>
            )}
          </div>

          <div className="form-field form-field-full">
            <label htmlFor="email">
              Email address <span>*</span>
            </label>

            <input
              id="email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="employee@example.com"
              className={
                getFieldError("email")
                  ? "input-error"
                  : ""
              }
              disabled={submitting}
            />

            {getFieldError("email") && (
              <small className="field-error">
                {getFieldError("email")}
              </small>
            )}
          </div>

          <div className="form-field">
            <label htmlFor="department">
              Department <span>*</span>
            </label>

            <select
              id="department"
              name="department"
              value={formData.department}
              onChange={handleChange}
              className={
                getFieldError("department")
                  ? "input-error"
                  : ""
              }
              disabled={submitting}
            >
              <option value="">
                Select department
              </option>

              <option value="Engineering">
                Engineering
              </option>

              <option value="Human Resources">
                Human Resources
              </option>

              <option value="Finance">
                Finance
              </option>

              <option value="Sales">
                Sales
              </option>

              <option value="Marketing">
                Marketing
              </option>

              <option value="Operations">
                Operations
              </option>

              <option value="Information Technology">
                Information Technology
              </option>
            </select>

            {getFieldError("department") && (
              <small className="field-error">
                {getFieldError("department")}
              </small>
            )}
          </div>

          <div className="form-field">
            <label htmlFor="jobTitle">
              Job title <span>*</span>
            </label>

            <input
              id="jobTitle"
              name="jobTitle"
              type="text"
              value={formData.jobTitle}
              onChange={handleChange}
              placeholder="Software Developer"
              className={
                getFieldError("jobTitle")
                  ? "input-error"
                  : ""
              }
              disabled={submitting}
            />

            {getFieldError("jobTitle") && (
              <small className="field-error">
                {getFieldError("jobTitle")}
              </small>
            )}
          </div>

          <div className="form-field">
            <label htmlFor="salary">
              Salary <span>*</span>
            </label>

            <input
              id="salary"
              name="salary"
              type="number"
              min="0"
              step="0.01"
              value={formData.salary}
              onChange={handleChange}
              placeholder="85000"
              className={
                getFieldError("salary")
                  ? "input-error"
                  : ""
              }
              disabled={submitting}
            />

            {getFieldError("salary") && (
              <small className="field-error">
                {getFieldError("salary")}
              </small>
            )}
          </div>

          <div className="form-field">
            <label htmlFor="hireDate">
              Hire date <span>*</span>
            </label>

            <input
              id="hireDate"
              name="hireDate"
              type="date"
              value={formData.hireDate}
              onChange={handleChange}
              className={
                getFieldError("hireDate")
                  ? "input-error"
                  : ""
              }
              disabled={submitting}
            />

            {getFieldError("hireDate") && (
              <small className="field-error">
                {getFieldError("hireDate")}
              </small>
            )}
          </div>

          <div className="form-field form-field-full">
            <label className="checkbox-label">
              <input
                name="active"
                type="checkbox"
                checked={formData.active}
                onChange={handleChange}
                disabled={submitting}
              />

              <span>Employee is active</span>
            </label>
          </div>
        </div>

        <div className="form-actions">
          <button
            type="button"
            className="secondary-button"
            onClick={handleReset}
            disabled={submitting}
          >
            {isEditMode
              ? "Restore Original Values"
              : "Reset"}
          </button>

          <button
            type="button"
            className="cancel-button"
            onClick={onCancel}
            disabled={submitting}
          >
            Cancel
          </button>

          <button
            type="submit"
            className="save-button"
            disabled={submitting}
          >
            {submitting
              ? isEditMode
                ? "Updating..."
                : "Saving..."
              : isEditMode
                ? "Update Employee"
                : "Save Employee"}
          </button>
        </div>
      </form>
    </section>
  );
}

export default EmployeeForm;