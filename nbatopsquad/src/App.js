import './App.css';
import Header from './Header';
import Home from './Home';
import TeamCreation from './Teams/TeamCreation';
import Transfer from './Teams/Transfer';
import TeamProvider from './Context/TeamSelection';
import LeagueView from './Leagues/LeagueView';
import Auth from './Auth/Auth';
import { Route, Router, Routes } from 'react-router-dom';
import ProtectedRoute from './Router/ProtectedRoutes';
import Main from './Main';
import EditTeam from './Teams/EditTeam';
import Teams from './Teams/Teams';
import AdminHome from './Admin/AdminHome';
import { useAuth } from './Context/AuthContext';




function App() {
  const {admin} = useAuth();

  return (
    <>
      {/* <Header/>
      <Home/> */}
      {/* <Auth/> */}
      {/* <LeagueView/> */}
      {/* <Transfer/> */}

      {/* <TeamProvider>
        <TeamCreation/>
      </TeamProvider> */}
      
      {/* <AdminHome/> */}
      

      <Routes>
        <Route path="/auth" element={<Auth/>}/>
      
        <Route path="/" element={<ProtectedRoute><Main/></ProtectedRoute>}>
        <Route index element={<ProtectedRoute><Home/></ProtectedRoute>}/>
        <Route path="/league/:leagueId" element={<ProtectedRoute><LeagueView/></ProtectedRoute>}/>
        <Route path="/transfers" element={<ProtectedRoute><Transfer/></ProtectedRoute>}/>
        <Route path="/team/view/:teamId" element={<ProtectedRoute><Teams/></ProtectedRoute>}/>
        <Route path="/team/edit" element={<ProtectedRoute><EditTeam/></ProtectedRoute>}/>
        <Route path="/team/create" element=
        {
        <ProtectedRoute>
          <TeamProvider>
            <TeamCreation/>
          </TeamProvider>
        </ProtectedRoute>
        }/>
        <Route path="/admin" element={<ProtectedRoute><AdminHome/></ProtectedRoute>}/>
        </Route>
      </Routes>
    </>
  );
}


export default App;
