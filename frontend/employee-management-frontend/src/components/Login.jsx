import { useState } from "react";

function Login({
  onLogin,
  loading,
  error,
}) {
  const [username, setUsername] =
    useState("");

  const [password, setPassword] =
    useState("");

  const handleSubmit = (event) => {
    event.preventDefault();

    if (
      !username.trim() ||
      !password
    ) {
      return;
    }

    onLogin({
      username: username.trim(),
      password,
    });
  };

  return (
    <div className="login-page">
      <div className="login-card">
        <div className="login-header">
          <h1>
            Employee Management System
          </h1>

          <p>
            Sign in to continue
          </p>
        </div>

        {error && (
          <div className="login-error">
            {error}
          </div>
        )}

        <form
          className="login-form"
          onSubmit={handleSubmit}
        >
          <div className="login-field">
            <label htmlFor="username">
              Username
            </label>

            <input
              id="username"
              type="text"
              value={username}
              onChange={(event) =>
                setUsername(
                  event.target.value
                )
              }
              placeholder="Enter username"
              disabled={loading}
            />
          </div>

          <div className="login-field">
            <label htmlFor="password">
              Password
            </label>

            <input
              id="password"
              type="password"
              value={password}
              onChange={(event) =>
                setPassword(
                  event.target.value
                )
              }
              placeholder="Enter password"
              disabled={loading}
            />
          </div>

          <button
            type="submit"
            className="login-button"
            disabled={loading}
          >
            {loading
              ? "Signing in..."
              : "Sign In"}
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;