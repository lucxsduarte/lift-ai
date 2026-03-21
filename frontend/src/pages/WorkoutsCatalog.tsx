import {useState, useEffect} from 'react';
import {useNavigate, Link} from 'react-router-dom';
import {GlassCard} from '../components/GlassCard';
import {api} from '../services/api';

interface Workout {
    id: string;
    name: string;
    exercises?: any[];
}

export function WorkoutsCatalog() {
    const [workouts, setWorkouts] = useState<Workout[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        async function fetchWorkouts() {
            try {
                const response = await api.get('/workouts');
                setWorkouts(response.data);
            } catch (error) {
                console.error('Erro ao buscar treinos:', error);
            } finally {
                setIsLoading(false);
            }
        }

        fetchWorkouts();
    }, []);

    const handleEdit = (workout: Workout) => {
        navigate(`/workouts/${workout.id}/edit`, {
            state: {workoutName: workout.name}
        });
    };

    return (
        <div className="space-y-6 pb-20">
            <Link to="/" className="text-zinc-400 hover:text-white transition-colors text-sm font-semibold flex items-center gap-2 w-fit">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <path d="m15 18-6-6 6-6"/>
                </svg>
                Voltar para o Início
            </Link>

            <div className="flex justify-between items-center px-2">
                <div>
                    <h1 className="text-3xl font-bold text-white tracking-tight">Meus Treinos</h1>
                    <p className="text-zinc-500 text-sm">Gerencie sua rotina de treinos</p>
                </div>
                <Link
                    to="/workouts/new"
                    className="bg-emerald-600 hover:bg-emerald-500 text-white p-3 rounded-xl shadow-lg shadow-emerald-900/20 transition-all active:scale-95"
                >
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                        <line x1="12" y1="5" x2="12" y2="19"></line>
                        <line x1="5" y1="12" x2="19" y2="12"></line>
                    </svg>
                </Link>
            </div>

            {isLoading ? (
                <div className="py-20 text-center text-zinc-600 animate-pulse">Buscando treinos no servidor...</div>
            ) : workouts.length === 0 ? (
                <GlassCard className="p-10 text-center">
                    <p className="text-zinc-400 mb-6">Você ainda não tem nenhum treino criado.</p>
                    <Link to="/workouts/new" className="text-emerald-400 font-bold hover:underline">
                        Criar meu primeiro treino ➜
                    </Link>
                </GlassCard>
            ) : (
                <div className="grid grid-cols-1 gap-4">
                    {workouts.map((workout, index) => (
                        <div
                            key={workout.id}
                            onClick={() => handleEdit(workout)}
                            className="cursor-pointer group animate-in fade-in slide-in-from-bottom-3 duration-500"
                            style={{animationDelay: `${index * 100}ms`}}
                        >
                            <GlassCard className="p-5 hover:border-emerald-500/30 transition-all active:scale-[0.98]">
                                <div className="flex justify-between items-center">
                                    <div className="flex flex-col">
                                        <span className="text-white font-bold text-lg group-hover:text-emerald-400 transition-colors">
                                            {workout.name}
                                        </span>
                                        <span className="text-xs text-zinc-500 uppercase tracking-widest mt-1">
                                            {workout.exercises?.length || 0} Exercícios cadastrados
                                        </span>
                                    </div>
                                    <div className="text-zinc-700 group-hover:text-emerald-500 transition-colors">
                                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                                            <path d="m9 18 6-6-6-6"/>
                                        </svg>
                                    </div>
                                </div>
                            </GlassCard>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}