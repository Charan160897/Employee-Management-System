import { useEffect, useState } from "react";

function EmployeeProfile({
  employee,
  onClose,
  onEdit,
}) {
  const [copied, setCopied] = useState(false);

  useEffect(() => {
    if (!employee) {
      return;
    }

    const handleKeyDown = (event) => {
      if (event.key === "Escape") {
        onClose();
      }
    };

    document.addEventListener(
      "keydown",
      handleKeyDown
    );

    const previousOverflow =
      document.body.style.overflow;

    document.body.style.overflow = "hidden";

    return () => {
      document.removeEventListener(
        "keydown",
        handleKeyDown
      );

      document.body.style.overflow =
        previousOverflow;
    };
  }, [employee, onClose]);

  if (!employee) {
    return null;
  }

  const employeeName =
    employee.fullName ||
    `${employee.firstName || ""} ${
      employee.lastName || ""
    }`.trim();

  const initials =
    `${employee.firstName?.[0] || ""}${
      employee.lastName?.[0] || ""
    }`.toUpperCase();

  const salary = Number(
    employee.salary || 0
  ).toLocaleString("en-US", {
    style: "currency",
    currency: "USD",
    maximumFractionDigits: 2,
  });

  const formattedHireDate =
    employee.hireDate
      ? new Date(
          `${employee.hireDate}T00:00:00`
        ).toLocaleDateString("en-US", {
          year: "numeric",
          month: "long",
          day: "numeric",
        })
      : "Not available";

  const handleOverlayClick = (event) => {
    if (
      event.target === event.currentTarget
    ) {
      onClose();
    }
  };

  const handleCopyEmail = async () => {
    try {
      await navigator.clipboard.writeText(
        employee.email
      );

      setCopied(true);

      setTimeout(() => {
        setCopied(false);
      }, 2000);
    } catch (error) {
      console.error(
        "Unable to copy email:",
        error
      );
    }
  };

  const handleEdit = () => {
    if (onEdit) {
      onEdit(employee);
    }


  };

  return (
    <div
      className="profile-overlay"
      onMouseDown={handleOverlayClick}
    >
      <section
        className="profile-card"
        role="dialog"
        aria-modal="true"
        aria-labelledby="employee-profile-title"
      >
        <button
          type="button"
          className="profile-close"
          onClick={onClose}
          aria-label="Close employee profile"
        >
          ×
        </button>

        <div className="profile-header">
          <div className="profile-avatar">
            {initials || "EM"}
          </div>

          <h2 id="employee-profile-title">
            {employeeName}
          </h2>

          <p className="profile-job-title">
            {employee.jobTitle ||
              "Job title unavailable"}
          </p>

          <span
            className={
              employee.active
                ? "profile-status profile-status-active"
                : "profile-status profile-status-inactive"
            }
          >
            {employee.active
              ? "Active"
              : "Inactive"}
          </span>
        </div>

        <div className="profile-section">
          <div className="profile-section-heading">
            <h3>
              Personal Information
            </h3>

            <span>
              Employee ID #{employee.id}
            </span>
          </div>

          <div className="profile-grid">
            <div className="profile-field">
              <span className="profile-field-label">
                First Name
              </span>

              <strong>
                {employee.firstName || "—"}
              </strong>
            </div>

            <div className="profile-field">
              <span className="profile-field-label">
                Last Name
              </span>

              <strong>
                {employee.lastName || "—"}
              </strong>
            </div>

            <div className="profile-field profile-field-wide">
              <span className="profile-field-label">
                Email
              </span>

              <div className="profile-email-row">
                <strong>
                  {employee.email || "—"}
                </strong>

                {employee.email && (
                  <button
                    type="button"
                    className="copy-email-button"
                    onClick={handleCopyEmail}
                  >
                    {copied
                      ? "Copied"
                      : "Copy"}
                  </button>
                )}
              </div>
            </div>
          </div>
        </div>

        <div className="profile-section">
          <div className="profile-section-heading">
            <h3>
              Employment Details
            </h3>
          </div>

          <div className="profile-grid">
            <div className="profile-field">
              <span className="profile-field-label">
                Department
              </span>

              <strong>
                {employee.department || "—"}
              </strong>
            </div>

            <div className="profile-field">
              <span className="profile-field-label">
                Job Title
              </span>

              <strong>
                {employee.jobTitle || "—"}
              </strong>
            </div>

            <div className="profile-field">
              <span className="profile-field-label">
                Salary
              </span>

              <strong>
                {salary}
              </strong>
            </div>

            <div className="profile-field">
              <span className="profile-field-label">
                Hire Date
              </span>

              <strong>
                {formattedHireDate}
              </strong>
            </div>
          </div>
        </div>

        <div className="profile-actions">
          <button
            type="button"
            className="profile-secondary-button"
            onClick={onClose}
          >
            Close
          </button>

          <button
            type="button"
            className="profile-edit-button"
            onClick={handleEdit}
          >
            Edit Employee
          </button>
        </div>
      </section>
    </div>
  );
}

export default EmployeeProfile;