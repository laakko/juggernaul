package main

import "fmt"
import "os"
import "bufio"

var currentId uint32

var todos Todos

func init() {
	os.Create("users.dat")
	os.Create("groups.dat")
	os.Create("todos.dat")
}

func pushToDB(o interface{}) {
	
	switch o.(type) {
		case User:
			var file, err = os.OpenFile("users.dat", os.O_APPEND|os.O_WRONLY, 0600)
			check (err)
			file.WriteString(fmt.Sprintf("%d,%s,%s,%s,%s,%d\n",
				o.(User).Id,
				o.(User).Name,
				o.(User).Email,
				o.(User).Password,
				o.(User).Token,
				o.(User).Groups[0]))
			fmt.Println("User pushed")
			file.Close()
		case Group:
			var file, err = os.OpenFile("groups.dat", os.O_APPEND|os.O_WRONLY, 0600)
			check(err)
			file.WriteString(fmt.Sprintf("%d,%s,%s,%d,%d,%d\n",
				o.(Group).Id,
				o.(Group).Name,
				o.(Group).Description,
				o.(Group).Created,
				o.(Group).Users[0],
				o.(Group).Todos[0]))
			fmt.Println("Group pushed")
			file.Close()
		case Todo:
			var file, err = os.OpenFile("todos.dat", os.O_APPEND|os.O_WRONLY, 0600)
			check(err)
			file.WriteString(fmt.Sprintf("%d,%s,%s,%d,%s,%s\n",
				o.(Todo).Id,
				o.(Todo).Name,
				o.(Todo).Description,
				o.(Todo).Status,
				o.(Todo).Created,
				o.(Todo).Due,
				o.(Todo).Priority,
				o.(Todo).Group,
				o.(Todo).Owner))
			fmt.Println("Todo item pushed")
			file.Close()
        default:
			fmt.Println("This is something else")
	}
}

func getFromDB(item interface{}) []interface{} {
	switch item.(type) {
		case User:
			return []interface{}{item.(User).Name,
				item.(User).Id,
				item.(User).Name,
				item.(User).Email,
				item.(User).Password,
				item.(User).Token,
				item.(User).Groups[0]))
		case Group:
			return []interface{}{item.(User).Name,
		case Todo:
			return []interface{}{item.(User).Name},
	}
}

func searchFromFile(Id uint32) []interface{}{

}

func removeFromDB() {

}

func check(e error) {
    if e != nil {
		panic(e)
    }
}

/*
// Give us some seed data
func init() {
	DbCreateTodo(Todo{Name: "Write presentation"})
	DbCreateTodo(Todo{Name: "Host meetup"})
}

func DbFindTodo(id uint32) Todo {
	for _, t := range todos {
		if t. Id == id {
			return t
		}
	}
	// return empty Todo if not found
	return Todo{}
}

//this is bad, I don't think it passes race condtions
func DbCreateTodo(t Todo) Todo {
	currentId += 1
	t.Id = currentId
	todos = append(todos, t)
	return t
}

func DbDestroyTodo(id uint32) error {
	for i, t := range todos {
		if t.Id == id {
			todos = append(todos[:i], todos[i+1:]...)
			return nil
		}
	}
	return fmt.Errorf("Could not find Todo with id of %d to delete", id)
}
*/