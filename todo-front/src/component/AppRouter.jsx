import { Box, Typography } from "@mui/material";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import App from "../App";
import Login from "./Login";

function Copyright() {
  <Typography variant="body2" color="textSecondary" align="center">
    {new Date().getFullYear()} {"Copyright @ "}새삭 마켓
  </Typography>;
}

function AppRouter() {
  return (
    <div>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<App />}></Route>
          <Route path="login" element={<Login />}></Route>
        </Routes>
      </BrowserRouter>
      <Box mt={5}>
        <Copyright />
      </Box>
    </div>
  );
}

export default AppRouter;
