import {type SyntheticEvent, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {GlassCard} from '../components/GlassCard';
import {api} from '../services/api';
import toast from 'react-hot-toast';

export function CreateWorkout() {
    const [name, setName] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e: SyntheticEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsLoading(true);

        try {
            const response = await api.post('/workouts', {name});

            const realWorkoutId = response.data.id;

            toast.success('Treino criado! Agora adicione os exercícios.');

            navigate(`/workouts/${realWorkoutId}/edit`, {
                state: {workoutName: name}
            });

        } catch (error) {
            console.error('Falha ao conectar com o backend:', error);
            toast.error('Erro ao criar treino. Verifique a conexão.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="space-y-6">
            <Link to="/workouts" className="text-zinc-400 hover:text-white transition-colors text-sm font-semibold flex items-center gap-2 w-fit">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <path d="m15 18-6-6 6-6"/>
                </svg>
                Voltar para o Catálogo
            </Link>

            <GlassCard className="p-6">
                <div className="mb-6">
                    <h2 className="text-2xl font-bold text-white mb-1">Novo Treino</h2>
                    <p className="text-zinc-400 text-sm">
                        Dê um nome para o seu treino. Na próxima etapa, você poderá adicionar os exercícios a ele.
                    </p>
                </div>

                <form onSubmit={handleSubmit} className="space-y-6">
                    <div className="space-y-2">
                        <label htmlFor="workoutName" className="block text-sm font-medium text-zinc-300 ml-1">
                            Nome do Treino <span className="text-emerald-500">*</span>
                        </label>
                        <input
                            id="workoutName"
                            type="text"
                            required
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            placeholder="Ex: Treino A - Peito e Tríceps"
                            disabled={isLoading}
                            className="w-full bg-zinc-900/50 border border-white/10 rounded-xl px-4 py-3 text-white placeholder-zinc-600 focus:outline-none focus:border-emerald-500/50 focus:ring-1 focus:ring-emerald-500/50 transition-all disabled:opacity-50"
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={isLoading || !name.trim()}
                        className="w-full bg-emerald-600/90 hover:bg-emerald-500/90 backdrop-blur-md text-white px-6 py-4 rounded-xl font-bold shadow-lg shadow-emerald-900/30 transition-all active:scale-95 border border-emerald-400/30 disabled:opacity-50 disabled:active:scale-100 flex justify-center items-center gap-2"
                    >
                        {isLoading ? (
                            <>
                                <svg className="animate-spin -ml-1 mr-2 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                </svg>
                                Criando Treino...
                            </>
                        ) : (
                            'Avançar para Exercícios ➜'
                        )}
                    </button>
                </form>
            </GlassCard>
        </div>
    );
}