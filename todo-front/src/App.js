import logo from "./logo.svg";
import "./App.css";
import Todo from "./component/Todo";
import { useEffect, useState } from "react";
import { Container, List, Paper } from "@mui/material";
import AddTodo from "./component/AddTodo";
import { call } from "./component/ApiService";

function App() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    call("/todo", "GET", null).then((response) => setItems(response.data));
  }, []);

  const addItem = (item) => {
    // item.id = "ID" + items.length;
    // item.done = false;
    // setItems([...items, item]);
    // console.log("===========items : " + items);
    call("/todo", "POST", item).then((response) => setItems(response.data));
  };
  const editItme = (item) => {
    call("/todo", "PUT", item).then((response) => setItems(response.data));
  };
  const deleteItem = (item) => {
    call("/todo", "DELETE", item).then((response) => setItems(response.data));
    // const newItems = items.filter((e) => e.id !== item.id);
    // setItems([...newItems]);
  };

  const todoItem = items && items.length > 0 && (
    <Paper style={{ margin: 16 }}>
      <List>
        {items.map((item) => (
          <Todo
            item={item}
            key={item.id}
            editItem={editItme}
            deleteItem={deleteItem}
          />
        ))}
      </List>
    </Paper>
  );
  return (
    <div className="App">
      <Container maxWidth="md">
        <AddTodo addItem={addItem} />
        <div className="TodoList">{todoItem}</div>
      </Container>
    </div>
  );
}

export default App;
