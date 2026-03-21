interface ConfirmModalProps {
    isOpen: boolean;
    title: string;
    message: string;
    confirmText?: string;
    cancelText?: string;
    isDestructive?: boolean;
    onConfirm: () => void;
    onCancel: () => void;
}

export function ConfirmModal({
                                 isOpen,
                                 title,
                                 message,
                                 confirmText = 'Confirmar',
                                 cancelText = 'Cancelar',
                                 isDestructive = false, // Se true, o botão de confirmar fica vermelho
                                 onConfirm,
                                 onCancel
                             }: ConfirmModalProps) {
    if (!isOpen) return null;

    return (
        // Overlay de fundo escuro e borrado (Z-INDEX ALTO para ficar acima de tudo)
        <div className="fixed inset-0 z-[200] flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-in fade-in duration-200">

            {/* O Card da Modal */}
            <div className="bg-zinc-900 border border-white/10 rounded-3xl p-6 w-full max-w-sm shadow-2xl animate-in zoom-in-95 duration-200">

                {/* Ícone de Alerta (Opcional, mas dá um toque premium) */}
                <div className={`w-12 h-12 rounded-full flex items-center justify-center mb-4 ${isDestructive ? 'bg-red-500/10 text-red-500' : 'bg-emerald-500/10 text-emerald-500'}`}>
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
                        <line x1="12" y1="9" x2="12" y2="13"/>
                        <line x1="12" y1="17" x2="12.01" y2="17"/>
                    </svg>
                </div>

                <h3 className="text-xl font-bold text-white mb-2">{title}</h3>
                <p className="text-zinc-400 text-sm mb-8 leading-relaxed">
                    {message}
                </p>

                <div className="flex gap-3 w-full">
                    <button
                        onClick={onCancel}
                        className="flex-1 bg-zinc-800 hover:bg-zinc-700 text-zinc-300 py-3 rounded-xl font-semibold transition-colors"
                    >
                        {cancelText}
                    </button>
                    <button
                        onClick={onConfirm}
                        className={`flex-1 py-3 rounded-xl font-semibold transition-colors ${
                            isDestructive
                                ? 'bg-red-500/80 hover:bg-red-500 text-white'
                                : 'bg-emerald-600 hover:bg-emerald-500 text-white'
                        }`}
                    >
                        {confirmText}
                    </button>
                </div>
            </div>
        </div>
    );
}