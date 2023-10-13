import React, {useState} from 'react'
import {Button, Grid, TextField} from "@mui/material";

const AddTodo = (props) => {
    const [item,setItem]=useState({title:""});
    const addItem =props.addItem;

    const onInputChange = (e) =>{
        setItem({title : e.target.value});
        console.log(item);
    }
    const onButtonClick = () =>{
        if(item.title !== ""){
        addItem(item);
        setItem({title : ""});
        }
    }
    const enterKeyEventHendler = (e) =>{
        if(e.keyCode === 13 && e.target.value !==""){
            onButtonClick();
        }
    }
    return (
        <Grid container style={{marginTop:20}}>
            <Grid xs={11} md={11} item style={{paddingRight:16}}>
                <TextField placeholder='Add Todo here' fullWidth
                           onChange={onInputChange}
                           onKeyDown={enterKeyEventHendler}
                           value={item.title} />
            </Grid>
            <Grid xs={1} md={1} item>
                <Button fullWidth style={{height:'100%'}} variant="outlined"
                        onClick={onButtonClick}> + </Button>
            </Grid>
        </Grid>
    )
}
export default AddTodo
