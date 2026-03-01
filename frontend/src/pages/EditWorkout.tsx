// src/pages/EditWorkout.tsx
import { useState, useEffect } from 'react';
import { useParams, Link, useLocation, useNavigate } from 'react-router-dom';
import { GlassCard } from '../components/GlassCard';
import { api } from '../services/api';

interface Exercise {
    id: string;
    name: string;
}

interface QueuedExercise {
    tempId: number;
    exerciseId: string;
    name: string;
    targetSets: number;
    targetReps: number;
    baseWeight: number;
}

export function EditWorkout() {
    const { id } = useParams();
    const navigate = useNavigate();
    const location = useLocation();

    // O nome vem do clique no catálogo, mas atualizamos caso o usuário recarregue a página
    const [workoutName, setWorkoutName] = useState(location.state?.workoutName || 'Carregando...');

    const [exercises, setExercises] = useState<Exercise[]>([]);
    const [addedExercises, setAddedExercises] = useState<QueuedExercise[]>([]);

    const [exerciseId, setExerciseId] = useState('');
    const [selectedExerciseName, setSelectedExerciseName] = useState('');
    const [targetSets, setTargetSets] = useState<string>('3');
    const [targetReps, setTargetReps] = useState<string>('10');
    const [baseWeight, setBaseWeight] = useState<string>('');

    const [isSaving, setIsSaving] = useState(false);
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);

    // 1. Busca o catálogo geral de exercícios para preencher o Dropdown
    useEffect(() => {
        async function fetchExercises() {
            try {
                const response = await api.get('/exercises');
                setExercises(response.data);
            } catch (error) {
                console.error('Erro ao buscar catálogo:', error);
            }
        }
        fetchExercises();
    }, []);

    // 2. Busca os dados DO TREINO atual para preencher a lista de exercícios já salvos
    useEffect(() => {
        async function loadExistingWorkout() {
            if (!id) return;
            try {
                const response = await api.get(`/workouts/${id}`);
                const workoutData = response.data;

                // Atualiza o nome real que veio do banco
                if (workoutData.name) {
                    setWorkoutName(workoutData.name);
                }

                // Se houver exercícios salvos, mapeia para o formato do Frontend
                if (workoutData.exercises && workoutData.exercises.length > 0) {
                    // Ordena pela ordem que veio do banco
                    const sortedExercises = workoutData.exercises.sort((a: any, b: any) => a.orderIndex - b.orderIndex);

                    const mappedExercises: QueuedExercise[] = sortedExercises.map((item: any, index: number) => ({
                        tempId: Date.now() + index,
                        exerciseId: item.exerciseId,
                        name: item.exerciseName,
                        targetSets: item.targetSets,
                        targetReps: item.targetReps,
                        baseWeight: item.baseWeight
                    }));

                    setAddedExercises(mappedExercises);
                }
            } catch (error) {
                console.error("Erro ao carregar os dados do treino:", error);
            }
        }
        loadExistingWorkout();
    }, [id]);

    // Filtros para Inputs Numéricos
    const handleSetsRepsChange = (e: React.ChangeEvent<HTMLInputElement>, setter: React.Dispatch<React.SetStateAction<string>>) => {
        let val = e.target.value;
        if (!/^\d*$/.test(val)) return;
        val = val.replace(/^0+/, '');
        if (val.length > 4) return;
        setter(val);
    };

    const handleWeightChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        let val = e.target.value.replace(',', '.');
        if (!/^\d*\.?\d*$/.test(val)) return;
        if (val.replace('.', '').length > 4) return;
        if (val.length > 1 && val.startsWith('0') && val[1] !== '.') {
            val = val.replace(/^0+/, '');
        }
        setBaseWeight(val);
    };

    const handleAddToQueue = (e: React.SyntheticEvent<HTMLFormElement>) => {
        e.preventDefault();

        const newItem: QueuedExercise = {
            tempId: Date.now(),
            exerciseId,
            name: selectedExerciseName,
            targetSets: Number(targetSets),
            targetReps: Number(targetReps),
            baseWeight: Number(baseWeight)
        };

        setAddedExercises(prev => [...prev, newItem]);
        setExerciseId('');
        setSelectedExerciseName('');
        setIsDropdownOpen(false);
    };

    const handleRemoveFromQueue = (tempId: number) => {
        setAddedExercises(prev => prev.filter(ex => ex.tempId !== tempId));
    };

    const moveExercise = (index: number, direction: 'up' | 'down') => {
        const newOrder = [...addedExercises];
        const targetIndex = direction === 'up' ? index - 1 : index + 1;

        if (targetIndex < 0 || targetIndex >= newOrder.length) return;

        [newOrder[index], newOrder[targetIndex]] = [newOrder[targetIndex], newOrder[index]];
        setAddedExercises(newOrder);
    };

    // 3. Salvamento Mestre em Lote (BULK PUT)
    const handleFinalSave = async () => {
        setIsSaving(true);
        try {
            // Prepara o Array exatamente no formato do BulkUpdateExercisesRequest do Java
            const payload = {
                exercises: addedExercises.map((ex, index) => ({
                    exerciseId: ex.exerciseId,
                    targetSets: ex.targetSets,
                    targetReps: ex.targetReps,
                    baseWeight: ex.baseWeight,
                    orderIndex: index // Salvamos a ordem exata em que aparecem na tela!
                }))
            };

            // Dispara um ÚNICO método PUT para o endpoint que recriamos
            await api.put(`/workouts/${id}/exercises`, payload);

            console.log('Treino atualizado em lote com sucesso no PostgreSQL!');
            navigate('/workouts');
        } catch (error) {
            console.error('Erro ao salvar treino:', error);
            alert('Ocorreu um erro ao salvar o treino no servidor.');
        } finally {
            setIsSaving(false);
        }
    };

    const isFormValid = exerciseId && targetSets !== '' && targetReps !== '' && baseWeight !== '';

    return (
        <div className="space-y-6 pb-32">
            <Link to="/workouts" className="text-zinc-400 hover:text-white transition-colors text-sm font-semibold flex items-center gap-2 w-fit">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <path d="m15 18-6-6 6-6"/>
                </svg>
                Voltar para o Catálogo
            </Link>

            <GlassCard className="p-6 !overflow-visible relative z-50">
                <div className="mb-8 border-b border-white/5 pb-6">
                    <h2 className="text-xs font-bold text-emerald-500 uppercase tracking-widest mb-1">Montando Treino</h2>
                    <h1 className="text-3xl font-bold text-white break-words">{workoutName}</h1>
                </div>

                <form onSubmit={handleAddToQueue} className="space-y-5">
                    <div className="space-y-2 relative">
                        <label className="block text-sm font-medium text-zinc-300 ml-1">Exercício <span className="text-emerald-500">*</span></label>

                        <button
                            type="button"
                            onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                            className="w-full bg-zinc-900/50 border border-white/10 rounded-xl px-4 py-3 text-left focus:outline-none focus:border-emerald-500/50 transition-all flex justify-between items-center"
                        >
                            <span className={exerciseId ? 'text-white font-medium' : 'text-zinc-600'}>
                                {selectedExerciseName || 'Selecione um exercício...'}
                            </span>
                            <svg className={`w-5 h-5 text-zinc-500 transition-transform ${isDropdownOpen ? 'rotate-180' : ''}`} fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 9l-7 7-7-7" />
                            </svg>
                        </button>

                        {isDropdownOpen && (
                            <div className="absolute left-0 right-0 z-[100] mt-2 bg-zinc-900/98 backdrop-blur-2xl border border-white/10 rounded-xl shadow-[0_20px_50px_rgba(0,0,0,1)] max-h-72 overflow-y-auto">
                                {exercises.map((ex) => (
                                    <button
                                        key={ex.id}
                                        type="button"
                                        className="w-full text-left px-4 py-4 text-zinc-300 hover:bg-emerald-600/20 hover:text-emerald-400 transition-all border-b border-white/5 last:border-0 active:bg-emerald-600/40"
                                        onClick={() => {
                                            setExerciseId(ex.id);
                                            setSelectedExerciseName(ex.name);
                                            setIsDropdownOpen(false);
                                        }}
                                    >
                                        {ex.name}
                                    </button>
                                ))}
                            </div>
                        )}
                    </div>

                    <div className="grid grid-cols-3 gap-3">
                        <div className="space-y-2">
                            <label className="text-sm text-zinc-400 block text-center">Séries</label>
                            <input
                                type="text" inputMode="numeric" placeholder="Ex: 3"
                                value={targetSets} onChange={(e) => handleSetsRepsChange(e, setTargetSets)}
                                className="w-full bg-zinc-900/50 border border-white/10 rounded-xl px-4 py-3 text-white text-center focus:border-emerald-500/50 outline-none placeholder:text-zinc-700"
                            />
                        </div>
                        <div className="space-y-2">
                            <label className="text-sm text-zinc-400 block text-center">Reps</label>
                            <input
                                type="text" inputMode="numeric" placeholder="Ex: 10"
                                value={targetReps} onChange={(e) => handleSetsRepsChange(e, setTargetReps)}
                                className="w-full bg-zinc-900/50 border border-white/10 rounded-xl px-4 py-3 text-white text-center focus:border-emerald-500/50 outline-none placeholder:text-zinc-700"
                            />
                        </div>
                        <div className="space-y-2">
                            <label className="text-sm text-zinc-400 block text-center">Peso</label>
                            <input
                                type="text" inputMode="decimal" placeholder="Ex: 20"
                                value={baseWeight} onChange={handleWeightChange}
                                className="w-full bg-zinc-900/50 border border-white/10 rounded-xl px-4 py-3 text-white text-center focus:border-emerald-500/50 outline-none placeholder:text-zinc-700"
                            />
                        </div>
                    </div>

                    <button
                        type="submit"
                        disabled={!isFormValid}
                        className="w-full mt-4 bg-zinc-800/40 hover:bg-zinc-800/80 text-emerald-400 py-4 rounded-xl font-bold border border-white/5 transition-all active:scale-[0.98] disabled:opacity-30 disabled:hover:bg-zinc-800/40"
                    >
                        + Adicionar à Lista
                    </button>
                </form>
            </GlassCard>

            {/* Renderiza a lista se houver exercícios OU um aviso limpo se a lista estiver vazia (útil para quando apaga todos) */}
            <div className={`space-y-4 relative z-10 transition-all duration-500 ${addedExercises.length > 0 ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4 pointer-events-none hidden'}`}>
                <h3 className="text-zinc-500 text-xs font-bold uppercase tracking-widest ml-2 mt-6">
                    Exercícios Selecionados ({addedExercises.length})
                </h3>

                {addedExercises.map((ex, index) => (
                    <div key={ex.tempId} className="flex items-center gap-4 bg-zinc-900/60 backdrop-blur-md border border-white/5 p-4 rounded-2xl animate-in fade-in slide-in-from-bottom-2">
                        <div className="flex flex-col gap-2 border-r border-white/5 pr-3">
                            <button
                                onClick={() => moveExercise(index, 'up')}
                                disabled={index === 0}
                                className="text-zinc-600 hover:text-emerald-400 disabled:opacity-20 transition-colors"
                            >
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"><path d="m18 15-6-6-6 6"/></svg>
                            </button>
                            <button
                                onClick={() => moveExercise(index, 'down')}
                                disabled={index === addedExercises.length - 1}
                                className="text-zinc-600 hover:text-emerald-400 disabled:opacity-20 transition-colors"
                            >
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"><path d="m6 9 6 6 6-6"/></svg>
                            </button>
                        </div>

                        <div className="flex-1 flex flex-col">
                            <span className="text-white font-bold">{ex.name}</span>
                            <span className="text-xs text-zinc-500">
                                {ex.targetSets} sets × {ex.targetReps} reps — {ex.baseWeight}kg
                            </span>
                        </div>

                        <button
                            onClick={() => handleRemoveFromQueue(ex.tempId)}
                            className="text-red-500/80 hover:text-red-400 transition-colors p-2.5 rounded-xl"
                        >
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                <path d="M3 6h18m-2 0v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6m3 0V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/>
                            </svg>
                        </button>
                    </div>
                ))}

                <button
                    onClick={handleFinalSave}
                    disabled={isSaving}
                    className="w-full bg-emerald-600 hover:bg-emerald-500 text-white py-5 rounded-2xl font-black shadow-xl shadow-emerald-900/20 transition-all active:scale-95 flex justify-center items-center gap-3 mt-6"
                >
                    {isSaving ? (
                        <>
                            <div className="h-5 w-5 border-2 border-white/30 border-t-white rounded-full animate-spin"></div>
                            SINCRONIZANDO...
                        </>
                    ) : 'SALVAR TREINO COMPLETO'}
                </button>
            </div>

            {/* Botão de salvar alternativo para permitir zerar o treino completamente e salvar ele vazio */}
            {addedExercises.length === 0 && (
                <button
                    onClick={handleFinalSave}
                    disabled={isSaving}
                    className="w-full mt-6 bg-zinc-800 text-zinc-400 py-5 rounded-2xl font-bold transition-all active:scale-95 flex justify-center items-center gap-3"
                >
                    {isSaving ? 'LIMPANDO...' : 'SALVAR TREINO VAZIO'}
                </button>
            )}
        </div>
    );
}