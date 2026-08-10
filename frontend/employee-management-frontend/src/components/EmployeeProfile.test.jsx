import {
  fireEvent,
  render,
  screen,
} from "@testing-library/react";

import EmployeeProfile from "./EmployeeProfile";

const employee = {
  id: 5,
  firstName: "Priya",
  lastName: "Patel",
  fullName: "Priya Patel",
  email: "priya@example.com",
  department: "Engineering",
  jobTitle: "Frontend Developer",
  salary: 97000,
  hireDate: "2026-04-10",
  active: true,
};

describe("EmployeeProfile", () => {
  test("renders employee profile", () => {
    render(
      <EmployeeProfile
        employee={employee}
        onClose={() => {}}
        onEdit={() => {}}
      />
    );

    expect(
      screen.getByText("Priya Patel")
    ).toBeInTheDocument();

    expect(
      screen.getAllByText("Frontend Developer")
    ).toHaveLength(2);

    expect(
      screen.getByText("$97,000.00")
    ).toBeInTheDocument();
  });

  test("calls onClose when close button clicked", () => {
  const onClose = vi.fn();

  render(
    <EmployeeProfile
      employee={employee}
      onClose={onClose}
      onEdit={() => {}}
    />
  );

  fireEvent.click(
    screen.getByRole(
      "button",
      {
        name: "Close employee profile",
      }
    )
  );

  expect(onClose)
    .toHaveBeenCalledTimes(1);
  });

  test("calls onEdit when Edit Employee is clicked", () => {
  const onEdit = vi.fn();

  render(
    <EmployeeProfile
      employee={employee}
      onClose={() => {}}
      onEdit={onEdit}
    />
  );

  fireEvent.click(
    screen.getByRole(
      "button",
      {
        name: "Edit Employee",
      }
    )
  );

  expect(onEdit)
    .toHaveBeenCalledWith(
      employee
    );
  });


});