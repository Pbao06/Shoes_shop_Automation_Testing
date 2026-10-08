

const user={
    id:10,
    username:"bao",
    age:20,
    isAdmin:false,
}
function getNameandAdmin()
{
    return user.username +" \n"+ user.isAdmin;
}
const valuee= getNameandAdmin();
console.log(valuee);

const userr = {
    id: 10,
    username: "bao",
    age: 20
};
// using detructuring
const {username,age}=userr;

const numbers = [1, 2, 3, 4, 5]; 
const newarray= numbers.map((x)=> x*2);
console.log(newarray);
const even= newarray.filter(x=>x%2==0);
console.log(even);

async function getAPI()
{
    try
    {
        const response = await fetch("https://localhost:5001/api/profile/GetUser");
        /// convert to type json 
        const data= await response.json();
        return data;
    }
    catch(error)
    {
        throw error;
    }
}

