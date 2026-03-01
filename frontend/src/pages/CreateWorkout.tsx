import {type SyntheticEvent, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {GlassCard} from '../components/GlassCard';
import {api} from '../services/api';

export function CreateWorkout() {
    const [name, setName] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    // Função disparada quando o usuário clica em Salvar
    // O 'React.' acessa a tipagem global sem precisar importar,
    // e o <HTMLFormElement> diz ao TypeScript exatamente de onde esse evento está vindo.
    const handleSubmit = async (e: SyntheticEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsLoading(true);

        try {
            // 2. A MÁGICA ACONTECE AQUI!
            // Fazendo um POST real para http://localhost:8080/api/workouts
            // O Axios já transforma o nosso { name } em JSON automaticamente
            const response = await api.post('/workouts', { name });

            console.log('Treino salvo no PostgreSQL:', response.data);

            // 3. Pegamos o ID real (UUID) que o seu backend Java gerou e devolveu
            const realWorkoutId = response.data.id;

            // 4. Redirecionamos para a tela de adicionar exercícios usando o ID de verdade
            navigate(`/workouts/${realWorkoutId}/edit`, {
                state: { workoutName: name }
            });

        } catch (error) {
            console.error('Falha ao conectar com o backend:', error);
            // Um alerta provisório só para avisar se o servidor estiver desligado
            alert('Erro ao criar treino. O seu Spring Boot está rodando?');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="space-y-6">

            {/* Botão de Voltar Minimalista */}
            <Link to="/workouts" className="text-zinc-400 hover:text-white transition-colors text-sm font-semibold flex items-center gap-2 w-fit">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <path d="m15 18-6-6 6-6"/>
                </svg>
                Voltar para o Catálogo
            </Link>

            <GlassCard className="p-6">

                {/* Cabeçalho do Formulário */}
                <div className="mb-6">
                    <h2 className="text-2xl font-bold text-white mb-1">Novo Treino</h2>
                    <p className="text-zinc-400 text-sm">
                        Dê um nome para o seu treino. Na próxima etapa, você poderá adicionar os exercícios a ele.
                    </p>
                </div>

                {/* Formulário */}
                <form onSubmit={handleSubmit} className="space-y-6">

                    {/* Campo de Input Estilizado (Liquid Glass) */}
                    <div className="space-y-2">
                        <label htmlFor="workoutName" className="block text-sm font-medium text-zinc-300 ml-1">
                            Nome do Treino <span className="text-emerald-500">*</span>
                        </label>
                        <input
                            id="workoutName"
                            type="text"
                            required // Validação HTML nativa (O backend já tem o @NotBlank)
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            placeholder="Ex: Treino A - Peito e Tríceps"
                            disabled={isLoading}
                            // O input tem um fundo semi-transparente e ganha um brilho emerald quando focado
                            className="w-full bg-zinc-900/50 border border-white/10 rounded-xl px-4 py-3 text-white placeholder-zinc-600 focus:outline-none focus:border-emerald-500/50 focus:ring-1 focus:ring-emerald-500/50 transition-all disabled:opacity-50"
                        />
                    </div>

                    {/* Botão de Submit com estado de Loading */}
                    <button
                        type="submit"
                        disabled={isLoading || !name.trim()}
                        className="w-full bg-emerald-600/90 hover:bg-emerald-500/90 backdrop-blur-md text-white px-6 py-4 rounded-xl font-bold shadow-lg shadow-emerald-900/30 transition-all active:scale-95 border border-emerald-400/30 disabled:opacity-50 disabled:active:scale-100 flex justify-center items-center gap-2"
                    >
                        {isLoading ? (
                            // Ícone de loading (Spinner) SVG
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