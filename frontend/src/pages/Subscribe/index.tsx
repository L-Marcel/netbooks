import styles from "./index.module.scss";
import useUser from "../../stores/useUser";
import { useState } from "react";

export default function Subscribe(){
    //const login = useUser();
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirm, setConfirm] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e : React.FormEvent) => {
        e.preventDefault();
        
        if (!email || !password) {
            alert("preencha os campos");
            // Alert para preenchimento de campos
            return;
        }
        if (password != confirm) {
            alert("senhas diferentes");
            // Alert para informar que a senha e a confirmação estão diferentes
            return
        }

        setIsLoading(true);
        try {
            alert("ok");
            // Alert de sucesso e redirecionamento de página
        } catch (error) {
            // Alert de erro
        }
        finally{
            setIsLoading(false);
        }
    }

    return(
        <div className={styles.container}>
            <div className={styles.box}>
                <div className={styles.text}>
                    <h1>Criar Conta</h1>
                    <p>Cadastre-se para acessar todos os recursos</p>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className={styles.form_group}>
                        <label htmlFor="name">Nome</label>
                        <input 
                            type="text" 
                            name="name" 
                            id="name" 
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            placeholder="Seu nome"
                            required
                        />
                    </div>

                    <div className={styles.form_group}>
                        <label htmlFor="email">Email</label>
                        <input 
                            type="email" 
                            name="email" 
                            id="email" 
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="seu@email.com"
                            required
                        />
                    </div>
                    
                    <div className={styles.form_group}>
                        <label htmlFor="password">Senha</label>
                        <input
                            type="password"
                            name="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="*****"
                            required
                        />
                    </div>
                    
                    <div className={styles.form_group}>
                        <label htmlFor="confirm">Confirmar senha</label>
                        <input
                            type="password"
                            name="confirm"
                            id="confirm"
                            value={confirm}
                            onChange={(e) => setConfirm(e.target.value)}
                            placeholder="*****"
                            required
                        />
                    </div>
                    
                    <div className={styles.form_group}>
                        <button 
                            type="submit" 
                            className={styles.submit}
                            disabled={isLoading}
                        >
                            {isLoading ? "Criando..." : "Criar conta"}
                        </button>
                    </div>
                    
                </form>
                <div className={styles.hasLogin}>
                    <p>
                        Já tem uma conta? <a href="#login" className={styles.login}>Entrar</a>
                    </p>
                </div>
            </div>
        </div>
            
    );
}