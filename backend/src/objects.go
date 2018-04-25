package main


type Todo struct {
	Id        	uint32	`json:"id"`
	Name      	string  `json:"name"`
	Description string	`json:"description"`
	Status	 	string	`json:"status"`
	Created   	uint64	`json:"created"`
	Due       	uint64  `json:"due"`
	Priority  	uint8	`json:"priority"`
	Group	  	string	`json:"group"`
	Owner		uint32	`json:"owner"`
}

type Group struct {
	Id        	uint32	`json:"id"`
	Name      	string  `json:"name"`
	Description string	`json:"description"`
	Created   	uint64	`json:"created"`
	Users []User
	Todos []Todo
}

type User struct {
	Id        	uint32	`json:"id"`
	Name      	string  `json:"name"`
	Email		string	`json:"email"`
	Password	string	`json:"password"`
	Token		string	`json:"token"`
	Groups []Group
}

type Todos []Todo
type Groups []Group
type Users []User