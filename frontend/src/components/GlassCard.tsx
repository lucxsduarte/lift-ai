import type {ReactNode} from 'react';

interface GlassCardProps {
    children: ReactNode; // Isso permite colocar qualquer coisa "dentro" do card
    className?: string;  // Permite injetar classes extras (como padding) quando precisarmos
}

export function GlassCard({ children, className = '' }: GlassCardProps) {
    return (
        // Reduzimos o arredondamento (rounded-2xl) para encaixar melhor nas bordas
        <div className={`liquid-glass bg-zinc-950/60 backdrop-blur-xl rounded-2xl border border-white/10 shadow-2xl relative overflow-hidden ${className}`}>

            {/* O brilho interno fica isolado aqui, nunca mais precisaremos repetir esse código */}
            <div className="absolute inset-0 opacity-10 bg-gradient-to-r from-emerald-500 via-zinc-950 to-indigo-500 liquid-flow"></div>

            {/* O conteúdo que passarmos vai renderizar aqui dentro */}
            <div className="relative z-10">
                {children}
            </div>

        </div>
    );
}