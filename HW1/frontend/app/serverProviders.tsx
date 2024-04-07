import { CookiesProvider } from "next-client-cookies/server";

export default function ServerProviders({
  children,
}: {
  children: React.ReactNode;
}) {
  return <CookiesProvider>{children}</CookiesProvider>;
}
