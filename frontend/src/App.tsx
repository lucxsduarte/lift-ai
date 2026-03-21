import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { DefaultLayout } from './layouts/DefaultLayout';
import { Dashboard } from './pages/Dashboard';
import { CreateWorkout } from './pages/CreateWorkout';
import { EditWorkout } from './pages/EditWorkout';
import { WorkoutsCatalog } from "./pages/WorkoutsCatalog";
import { Toaster } from "react-hot-toast";

export default function App() {
    return (
        <>
            <Toaster
                position="top-center"
                reverseOrder={false}
                toastOptions={{
                    style: {
                        background: 'rgba(24, 24, 27, 0.9)',
                        color: '#fff',
                        backdropFilter: 'blur(8px)',
                        border: '1px solid rgba(255, 255, 255, 0.1)',
                        borderRadius: '16px',
                        padding: '16px',
                        fontWeight: '500',
                    },
                    success: {
                        iconTheme: {
                            primary: '#10b981',
                            secondary: '#18181b',
                        },
                    },
                    error: {
                        iconTheme: {
                            primary: '#ef4444',
                            secondary: '#18181b',
                        },
                    },
                }}
            />

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
        </>
    );
}