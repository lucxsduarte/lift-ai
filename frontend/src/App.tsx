import { Header } from './components/Header';
import { GlassCard } from './components/GlassCard';

export default function App() {
    return (
        <div className="min-h-screen font-sans text-gray-200">

            <Header />

            <main className="px-3 pt-24 pb-6 max-w-md mx-auto">

                <GlassCard className="p-6">

                    <div className="flex flex-col items-center text-center">

                        <div className="text-5xl mb-4 h-16 w-16 flex items-center justify-center rounded-full bg-zinc-900/80 border border-white/5 shadow-inner">
                            🎯
                        </div>

                        <h2 className="text-xl font-bold text-white mb-2">Vamos começar?</h2>

                        <p className="text-zinc-400 mb-8 text-sm leading-relaxed">
                            Para montar a sua semana de exercícios, você primeiro precisa criar os seus treinos (Ex: Treino A, Treino B).
                        </p>

                        <div className="w-full space-y-3">

                            <button className="w-full bg-emerald-600/80 hover:bg-emerald-500/80 backdrop-blur-md text-white px-6 py-4 rounded-xl font-bold shadow-lg shadow-emerald-900/30 transition-all active:scale-95 border border-emerald-400/30">
                                1. Criar um Treino
                            </button>

                            <button className="w-full bg-zinc-800/60 hover:bg-zinc-700/60 backdrop-blur-md text-zinc-300 px-6 py-4 rounded-xl font-bold transition-all active:scale-95 border border-white/5">
                                2. Montar Cronograma
                            </button>

                        </div>
                    </div>

                </GlassCard>

            </main>
        </div>
    );
}