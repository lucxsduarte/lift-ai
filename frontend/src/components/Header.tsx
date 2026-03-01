export function Header() {
    return (
        <header className="fixed top-0 left-0 right-0 z-50 bg-black/40 backdrop-blur-xl border-b border-white/10 text-white p-4 flex justify-between items-center shadow-lg shadow-black/30">

            {/* Esquerda: Burger e Título */}
            <div className="flex items-center gap-4">

                {/* Ícone de Hamburger (Menu) SVG */}
                <button className="text-gray-300 hover:text-white transition-colors focus:outline-none">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <line x1="4" x2="20" y1="12" y2="12" />
                        <line x1="4" x2="20" y1="6" y2="6" />
                        <line x1="4" x2="20" y1="18" y2="18" />
                    </svg>
                </button>

                {/* LIFT AI */}
                <h1 className="text-xl font-bold tracking-widest text-white">LIFT AI</h1>
            </div>

            {/* Direita: Notificações e Avatar */}
            <div className="flex items-center gap-5">

                {/* Ícone de Notificações (Sino) SVG */}
                <button className="text-gray-300 hover:text-white relative focus:outline-none transition-colors">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9" />
                        <path d="M10.3 21a1.94 1.94 0 0 0 3.4 0" />
                    </svg>
                    {/* Indicador de notificação não lida */}
                    <span className="absolute -top-0.5 -right-0.5 block h-2 w-2 rounded-full bg-emerald-500 shadow-[0_0_8px_rgba(16,185,129,0.8)]"></span>
                </button>

                {/* Waffle / Grid Icon SVG (Menu de Apps) */}
                <button className="text-gray-300 hover:text-white relative focus:outline-none transition-colors">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <rect width="7" height="7" x="3" y="3" rx="1" />
                        <rect width="7" height="7" x="14" y="3" rx="1" />
                        <rect width="7" height="7" x="14" y="14" rx="1" />
                        <rect width="7" height="7" x="3" y="14" rx="1" />
                    </svg>
                </button>

                {/* Avatar do Usuário (Padronizado sem borda) */}
                <button className="text-gray-300 hover:text-white relative focus:outline-none transition-colors">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2" />
                        <circle cx="12" cy="7" r="4" />
                    </svg>
                </button>

            </div>

        </header>
    );
}