-- inser roles
insert into "roles" 
("id", "name", "description") 
values 
(1, 'FULL_ACCESS', 'Can view, edit, configure, and add collaborators to the project'),
(2, 'COLLABORATORS', 'Can access, view, and add/edit project items but cannot configure project'),
(3, 'VIEWERS', 'Can only view and comment, but cannot add or edit project');