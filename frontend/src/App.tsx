import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { DefaultLayout } from './layouts/DefaultLayout';
import { Dashboard } from './pages/Dashboard';
import { CreateWorkout } from './pages/CreateWorkout';
import { EditWorkout } from './pages/EditWorkout';
import {WorkoutsCatalog} from "./pages/WorkoutsCatalog.tsx";

export default function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<DefaultLayout />}>
                    <Route index element={<Dashboard />} />
                    <Route path="workouts" element={<WorkoutsCatalog />} />
                    <Route path="workouts/new" element={<CreateWorkout />} />
                    <Route path="workouts/:id/edit" element={<EditWorkout />} />
                </Route>
            </Routes>
        </BrowserRouter>
    );
}